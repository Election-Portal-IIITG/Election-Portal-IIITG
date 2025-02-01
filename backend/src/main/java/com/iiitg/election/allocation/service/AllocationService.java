package com.iiitg.election.allocation.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.iiitg.election.allocation.Allocation;
import com.iiitg.election.allocation.Classroom;
import com.iiitg.election.allocation.Slot;
import com.iiitg.election.allocation.SlotClassroom;
import com.iiitg.election.allocation.dto.AllocationDto;
import com.iiitg.election.allocation.dto.AllocationDtoMapper;
import com.iiitg.election.allocation.jpa.ClassroomSpringDataJpaRepository;
import com.iiitg.election.allocation.jpa.SlotClassroomSpringDataJpaRepository;
import com.iiitg.election.allocation.jpa.SlotSpringDataJpaRepository;
import com.iiitg.election.faculty.Faculty;
import com.iiitg.election.faculty.jpa.FacultySpringDataJpaRepository;
import com.iiitg.election.student.Student;
import com.iiitg.election.student.jpa.StudentSpringDataJpaRepository;

@Service
public class AllocationService {
    
    private ClassroomSpringDataJpaRepository classroomRepository;
    private StudentSpringDataJpaRepository studentRepository;
    private FacultySpringDataJpaRepository facultyRepository;
    private SlotSpringDataJpaRepository slotRepository;
    private SlotClassroomSpringDataJpaRepository slotClassroomRepository;
    private AllocationDtoMapper allocationDtoMapper;
    private Allocation allocationProcessor;

	public AllocationService(ClassroomSpringDataJpaRepository classroomRepository,
			StudentSpringDataJpaRepository studentRepository, FacultySpringDataJpaRepository facultyRepository,
			SlotSpringDataJpaRepository slotRepository, SlotClassroomSpringDataJpaRepository slotClassroomRepository,
			AllocationDtoMapper allocationDtoMapper, Allocation allocationProcessor) {
		super();
		this.classroomRepository = classroomRepository;
		this.studentRepository = studentRepository;
		this.facultyRepository = facultyRepository;
		this.slotRepository = slotRepository;
		this.slotClassroomRepository = slotClassroomRepository;
		this.allocationDtoMapper = allocationDtoMapper;
		this.allocationProcessor = allocationProcessor;
	}

	public List<AllocationDto> getAllocations() {
        List<Classroom> classrooms = classroomRepository.findAll();
        return classrooms.stream()
        		.filter(classroom -> classroom.getAssignedFaculty() != null)
                .map(classroom -> {
                    List<Student> students = studentRepository
                        .findBySlotClassroom_Classroom_ClassroomName(classroom.getClassroomName());
                    return allocationDtoMapper.toDto(classroom, students);
                })
                .collect(Collectors.toList());
    }
    
    public List<AllocationDto> getAllocationsByClassroom(String classroomName) {
        Classroom classroom = classroomRepository.findByClassroomName(classroomName);
        if (classroom == null) {
            throw new RuntimeException("Classroom not found: " + classroomName);
        }
        
        List<Student> students = studentRepository
            .findBySlotClassroom_Classroom_ClassroomName(classroomName);
            
        return List.of(allocationDtoMapper.toDto(classroom, students));
    }
    
    public void allocate(LocalDateTime votingStartTime, int slotDuration, int gapDuration) {
        List<Classroom> classrooms = classroomRepository.findByIsAvailable(true);
        List<Faculty> faculties = facultyRepository.findByIsAvailable(true);
        List<Student> students = studentRepository.findByOnCampus(true);
        
        // Calculate required classrooms based on student count and classroom capacities
        classrooms.sort((c1, c2) -> Integer.compare(c2.getCapacity(), c1.getCapacity()));
        int remainingStudents = students.size();
        int requiredClassrooms = 0;
        
        for (Classroom classroom : classrooms) {
            if (remainingStudents <= 0) break;
            remainingStudents -= classroom.getCapacity();
            requiredClassrooms++;
        }
        
        // Select only the needed number of classrooms
        List<Classroom> selectedClassrooms = classrooms.subList(0, requiredClassrooms);
        int totalCapacity = selectedClassrooms.stream().mapToInt(Classroom::getCapacity).sum();
        int requiredSlots = (int) Math.ceil((double) students.size() / totalCapacity);
        
        List<Slot> slots = allocationProcessor.generateSlots(votingStartTime, requiredSlots, slotDuration, gapDuration);
        slotRepository.saveAll(slots);
        
        List<SlotClassroom> slotClassrooms = allocationProcessor.assignStudentsToClassrooms(students, selectedClassrooms, slots);
        slotClassroomRepository.saveAll(slotClassrooms);
        
        Map<Student, SlotClassroom> studentSlotMapping = allocationProcessor.mapStudentsToSlotClassroom(students, slotClassrooms);
        for (Map.Entry<Student, SlotClassroom> entry : studentSlotMapping.entrySet()) {
            entry.getKey().setSlotClassroom(entry.getValue());
        }
        studentRepository.saveAll(students);
        
        // Only assign faculties to classrooms that have students
        Set<Classroom> activeClassrooms = slotClassrooms.stream()
            .map(SlotClassroom::getClassroom)
            .collect(Collectors.toSet());
        
        allocationProcessor.assignFaculties(new ArrayList<>(activeClassrooms), faculties);
        classroomRepository.saveAll(selectedClassrooms);
    }

    
    
}