package com.iiitg.election.allocation.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.iiitg.election.allocation.Classroom;
import com.iiitg.election.allocation.Slot;
import com.iiitg.election.faculty.Faculty;
import com.iiitg.election.student.Student;

@Component
public class AllocationDtoMapper {
    
    public AllocationDto toDto(Classroom classroom, List<Student> students) {
        AllocationDto dto = new AllocationDto();
        
        // Map Classroom Info
        AllocationDto.ClassroomInfo classroomInfo = new AllocationDto.ClassroomInfo();
        classroomInfo.setClassroomName(classroom.getClassroomName());
        classroomInfo.setCapacity(classroom.getCapacity());
        dto.setClassroom(classroomInfo);
        
        // Map Election Slots with their students
        Map<Slot, List<Student>> studentsBySlot = students.stream()
            .collect(Collectors.groupingBy(
                student -> student.getSlotClassroom().getSlot()
            ));
            
        List<AllocationDto.ElectionSlotsInfo> electionSlotsInfoList = classroom.getClassroomSlots().stream()
                .map(slotClassroom -> {
                    AllocationDto.ElectionSlotsInfo slotInfo = new AllocationDto.ElectionSlotsInfo();
                    Slot slot = slotClassroom.getSlot();
                    slotInfo.setSlotStartTime(slot.getSlotStartTime());
                    slotInfo.setSlotEndTime(slot.getSlotEndTime());

                    Faculty assignedFaculty = slotClassroom.getAssignedFaculty();
                    
                    if (assignedFaculty != null) {
                        AllocationDto.ElectionSlotsInfo.FacultyInfo facultyInfo = new AllocationDto.ElectionSlotsInfo.FacultyInfo();
                        facultyInfo.setFirstName(assignedFaculty.getFirstName());
                        facultyInfo.setLastName(assignedFaculty.getLastName());
                        facultyInfo.setFacultyEmailId(assignedFaculty.getFacultyEmailId());
                        slotInfo.setFaculty(facultyInfo);
                    }
                    
                    // Get students for this slot
                    List<Student> slotStudents = studentsBySlot.getOrDefault(slot, List.of());
                    List<AllocationDto.ElectionSlotsInfo.StudentInfo> votersList = slotStudents.stream()
                            .map(student -> {
                                AllocationDto.ElectionSlotsInfo.StudentInfo studentInfo = 
                                    new AllocationDto.ElectionSlotsInfo.StudentInfo();
                                studentInfo.setStudentEmailId(student.getStudentEmailId());
                                studentInfo.setRollNumber(student.getRollNumber());
                                studentInfo.setFirstName(student.getFirstName());
                                studentInfo.setLastName(student.getLastName());
                                return studentInfo;
                            })
                            .collect(Collectors.toList());
                    
                    slotInfo.setVoters(votersList);
                    return slotInfo;
                })
                .collect(Collectors.toList());
        
        dto.setElectionSlots(electionSlotsInfoList);
        
        return dto;
    }
}