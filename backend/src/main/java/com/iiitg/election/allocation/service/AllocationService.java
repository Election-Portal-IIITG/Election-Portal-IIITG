package com.iiitg.election.allocation.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iiitg.election.allocation.Classroom;
import com.iiitg.election.allocation.Slot;
import com.iiitg.election.allocation.SlotClassroom;
import com.iiitg.election.allocation.dto.AllocationDto;
import com.iiitg.election.allocation.dto.AllocationDtoMapper;
import com.iiitg.election.allocation.exceptions.ResourceUnavailableException;
import com.iiitg.election.allocation.exceptions.SlotAllocationFailureException;
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

	public AllocationService(ClassroomSpringDataJpaRepository classroomRepository,
			StudentSpringDataJpaRepository studentRepository, FacultySpringDataJpaRepository facultyRepository,
			SlotSpringDataJpaRepository slotRepository, SlotClassroomSpringDataJpaRepository slotClassroomRepository,
			AllocationDtoMapper allocationDtoMapper) {
		super();
		this.classroomRepository = classroomRepository;
		this.studentRepository = studentRepository;
		this.facultyRepository = facultyRepository;
		this.slotRepository = slotRepository;
		this.slotClassroomRepository = slotClassroomRepository;
		this.allocationDtoMapper = allocationDtoMapper;
	}

	/**
	 * Retrieves all voting slot allocations across all classrooms that have been assigned.
	 * 
	 * <p>This method returns a comprehensive list of allocation details for all classrooms
	 * that have active voting slot assignments. Only classrooms with at least one allocated
	 * slot are included in the results, ensuring the response contains only meaningful
	 * allocation data.
	 * 
	 * <p><strong>Response Structure:</strong>
	 * Each {@link AllocationDto} in the returned list contains:
	 * <ul>
	 *   <li>Classroom details (name, capacity)</li>
	 *   <li>Associated time slot information</li>
	 *   <li>List of students assigned to vote in that slot-classroom</li>
	 *   <li>Faculty supervision details</li>
	 * </ul>
	 * 
	 * @return a list of {@link AllocationDto} objects representing all classroom
	 *         allocations with their associated students and slot details.
	 *         Returns an empty list if no allocations have been made.
	 *         Never returns null.
	 * 
	 * @author Ishaan Das
	 */
	public List<AllocationDto> getAllocations() {
        List<Classroom> classrooms = classroomRepository.findAll();
        return classrooms.stream()
        		.filter(classroom -> classroom.getClassroomSlots().size() != 0)
                .map(classroom -> {
                    List<Student> students = studentRepository
                        .findBySlotClassroom_Classroom_ClassroomName(classroom.getClassroomName());
                    return allocationDtoMapper.toDto(classroom, students);
                })
                .collect(Collectors.toList());
    }
    
	/**
	 * Retrieves voting slot allocation details for a specific classroom.
	 * 
	 * <p>This method provides detailed allocation information for a single classroom,
	 * including all students assigned to vote in that classroom across all time slots.
	 * The method is useful for classroom-specific queries and detailed allocation reviews.
	 * 
	 * <p><strong>Response Structure:</strong>
	 * The returned {@link AllocationDto} contains:
	 * <ul>
	 *   <li>Classroom details (name, capacity)</li>
	 *   <li>Associated time slot information</li>
	 *   <li>List of students assigned to vote in that slot-classroom</li>
	 *   <li>Faculty supervision details</li>
	 * </ul>
	 * 
	 * <p><strong>Error Handling:</strong>
	 * The method performs strict validation to ensure data integrity and provide
	 * clear error messages for invalid requests.
	 * 
	 * @param classroomName the exact name of the classroom to query allocation details for.
	 *                     Must not be null or empty. Case-sensitive match is performed
	 *                     against the classroom name stored in the database.
	 * 
	 * @return a single-element list containing the {@link AllocationDto} for the
	 *         specified classroom with all associated allocation details.
	 *         Returns a list (rather than a single object) to maintain consistency
	 *         with the {@link #getAllocations()} method return type.
	 * 
	 * @throws RuntimeException        if the specified classroom name is not found
	 *                                in the database. The exception message will
	 *                                include the classroom name that was not found.
	 * 
	 * @author Ishaan Das
	 */
    public List<AllocationDto> getAllocationsByClassroom(String classroomName) throws RuntimeException{
        Classroom classroom = classroomRepository.findByClassroomName(classroomName);
        if (classroom == null) {
            throw new RuntimeException("Classroom not found: " + classroomName);
        }
        
        List<Student> students = studentRepository
            .findBySlotClassroom_Classroom_ClassroomName(classroomName);
            
        return List.of(allocationDtoMapper.toDto(classroom, students));
    }

    /**
     * Allocates voting time slots for on-campus students by automatically assigning them to 
     * available classrooms with faculty supervision. This method creates multiple time slots
     * as needed to accommodate all eligible students.
     * 
     * <p>The allocation process follows these steps:
     * <ol>
     *   <li>Validates availability of required resources (students, faculties, classrooms)</li>
     *   <li>Randomizes the order of classrooms and faculties for fair distribution</li>
     *   <li>Sorts classrooms by capacity in descending order for optimal utilization</li>
     *   <li>Creates time slots iteratively until all students are assigned</li>
     *   <li>For each slot, assigns classrooms and faculties using a greedy allocation strategy</li>
     *   <li>Selects optimal classroom capacity to minimize unused seats</li>
     * </ol>
     * 
     * <p><strong>Resource Requirements:</strong>
     * <ul>
     *   <li>At least one on-campus student must be available</li>
     *   <li>At least one faculty member must be available for supervision</li>
     *   <li>At least one classroom must be available for allocation</li>
     * </ul>
     * 
     * <p><strong>Allocation Strategy:</strong>
     * The method uses a greedy approach to maximize resource utilization:
     * <ul>
     *   <li>Classrooms are selected based on optimal capacity matching</li>
     *   <li>Each slot continues allocation until resources are exhausted</li>
     *   <li>Time slots are created with specified duration and gap intervals</li>
     * </ul>
     * 
     * <p><strong>Database Operations:</strong>
     * This method performs multiple database transactions including:
     * <ul>
     *   <li>Creating and saving new {@link Slot} entities</li>
     *   <li>Creating and saving {@link SlotClassroom} mappings</li>
     *   <li>Assigning faculty members to slot-classroom combinations</li>
     *   <li>Assigning students to their allocated slot-classroom combinations</li>
     * </ul>
     * 
     * <p><strong>Transaction Behavior:</strong>
     * All database operations are executed within a single transaction. If any error occurs
     * during the allocation process, all changes are automatically rolled back to maintain
     * data consistency.
     * 
     * @param votingStartTime the start date and time for the first voting slot.
     *                       All subsequent slots will be scheduled after this time
     *                       with appropriate duration and gap intervals.
     * @param slotDuration   the duration of each voting slot in minutes.
     *                       Must be a positive integer.
     * @param gapDuration    the gap duration between consecutive voting slots in minutes.
     *                       Must be a non-negative integer. A value of 0 means
     *                       consecutive slots with no gap.
     * 
     * @throws ResourceUnavailableException if any required resources are unavailable:
     *                                     <ul>
     *                                       <li>No on-campus students found</li>
     *                                       <li>No available faculty members found</li>
     *                                       <li>No available classrooms found</li>
     *                                     </ul>
     *                                     The exception message will specify which resources are missing.
     * 
     * @throws SlotAllocationFailureException if the allocation process fails during execution:
     *                                       <ul>
     *                                         <li>No students could be assigned to a time slot</li>
     *                                         <li>Insufficient faculties or classrooms for remaining students</li>
     *                                       </ul>
     *                                       The exception provides detailed information about the failure point,
     *                                       including unassigned student count and resource utilization.
     * 
     * @throws RuntimeException for any other unexpected errors during the allocation process.
     *                         All database changes will be rolled back due to the transactional nature.
     * 
     * 
     * @author Ishaan Das
     */
    @Transactional
    public void allocate(LocalDateTime votingStartTime, int slotDuration, int gapDuration) throws RuntimeException {
        List<Classroom> availableClassrooms = classroomRepository.findByIsAvailable(true);
        List<Faculty> availableFaculties = facultyRepository.findByIsAvailable(true);
        List<Student> onCampusStudents = studentRepository.findByOnCampus(true);
        
        //Throwing ResourceUnavailableException
        if (onCampusStudents.isEmpty() || availableFaculties.isEmpty() || availableClassrooms.isEmpty()) {
        	List<String> missing = new ArrayList<>();
        	if (onCampusStudents.isEmpty()) missing.add("No on-campus students");
        	if (availableFaculties.isEmpty()) missing.add("No available faculties");
        	if (availableClassrooms.isEmpty()) missing.add("No available classrooms");

        	if (!missing.isEmpty()) {
        	    throw new ResourceUnavailableException("Allocation failed: " + String.join(", ", missing));
        	}
        }
        
        //Shuffle to create randomness
        Collections.shuffle(availableClassrooms);
        Collections.shuffle(availableFaculties);
        
        //Sort classrooms by capacity in descending order
        availableClassrooms.sort((c1,c2) -> Integer.compare(c2.getCapacity(), c1.getCapacity()));
        
        
        int numberOfUnassignedStudents = onCampusStudents.size();
        LocalDateTime currentTime = votingStartTime;
        int totalStudentsProcessed = 0;
        
        while(numberOfUnassignedStudents > 0) {
        	//Create new time slot
        	Slot newTimeSlot = new Slot(currentTime, currentTime.plusMinutes(slotDuration));
        	slotRepository.save(newTimeSlot);
        	
        	int classroomsUsedInThisSlot = 0;
        	int facultiesUsedInThisSlot = 0;
        	
        	//Reset pointers for new time slot
        	int classroomPtr = 0;
        	int facultyPtr = 0;
        	int studentsAssignedInThisSlot = 0;
        	
        	//Continue until we run out of classrooms or faculties or students
        	while(classroomPtr < availableClassrooms.size() &&
        			facultyPtr < availableFaculties.size() &&
        			numberOfUnassignedStudents > 0
        			) {
        		
        		Classroom currentClassroom = availableClassrooms.get(classroomPtr);
        		
        		//find the best classroom
        		while (classroomPtr + 1 < availableClassrooms.size() &&
        			       availableClassrooms.get(classroomPtr + 1).getCapacity() >= numberOfUnassignedStudents) {
        			    classroomPtr++;
        			}
        			currentClassroom = availableClassrooms.get(classroomPtr);
        			classroomsUsedInThisSlot++;
        		
        		//Finding a faculty to assign
        		Faculty currentFaculty = availableFaculties.get(facultyPtr);
        		facultiesUsedInThisSlot++;
        		
        		//Map classroom with time-slot
        		SlotClassroom newSlotClassroom = new SlotClassroom(currentClassroom, newTimeSlot);
        		slotClassroomRepository.save(newSlotClassroom);
        		
        		//Assign Faculty to slot-classroom
        		assignFaculty(newSlotClassroom, currentFaculty);
        		
        		//Calculate number of students to assign to this classroom
        		int numberOfStudentsToAssign = Math.min(currentClassroom.getCapacity(), numberOfUnassignedStudents);
        		int endIndex = Math.min(totalStudentsProcessed + numberOfStudentsToAssign, onCampusStudents.size());
        		
                // Fetch students and assign
                List<Student> studentsToAllot = onCampusStudents.subList(totalStudentsProcessed, endIndex);
                assignSlotClassroomsToStudents(newSlotClassroom, studentsToAllot);
                
                // Update counters
                numberOfUnassignedStudents -= numberOfStudentsToAssign;
                studentsAssignedInThisSlot += numberOfStudentsToAssign;
                totalStudentsProcessed += numberOfStudentsToAssign;
                
                // Move pointers to next available resources
                classroomPtr++;
                facultyPtr++;
        		
        	}
        	
        	//Throwing SlotAllocationFailureException
        	if (studentsAssignedInThisSlot == 0) {
        	    throw new SlotAllocationFailureException("Allocation halted at time slot starting " + currentTime +
        	        ": no students assigned. Possible reasons: ran out of faculties or classrooms.\n" +
        	        "Unassigned students remaining: " + numberOfUnassignedStudents + "\n" +
        	        "Classrooms used in this slot: " + classroomsUsedInThisSlot + " / " + availableClassrooms.size() + "\n" +
        	        "Faculties used in this slot: " + facultiesUsedInThisSlot + " / " + availableFaculties.size());
        	}
        	
            // Move to next time slot
            currentTime = currentTime.plusMinutes(slotDuration).plusMinutes(gapDuration);
        }
    }
        
    
    /**
     * Assigns a faculty member to supervise a specific slot-classroom combination.
     * 
     * <p>This method establishes the supervision relationship between a faculty member
     * and a slot-classroom pairing, ensuring that each voting session has proper
     * faculty oversight. The assignment is immediately persisted to the database.
     * 
     * <p><strong>Database Impact:</strong>
     * Updates the {@code assigned_faculty} field in the slot_classroom table and
     * immediately saves the changes to ensure data consistency.
     * 
     * @param slotClassroom the slot-classroom combination that requires faculty supervision.
     *                     Must not be null and should be a valid, previously saved entity.
     * @param faculty the faculty member to assign as supervisor for this slot-classroom.
     *                     Must not be null and should be an available faculty member.
     * 
     * @author Ishaan Das
     */
    private void assignFaculty(SlotClassroom slotClassroom, Faculty faculty) {
    	slotClassroom.setAssignedFaculty(faculty);
    	slotClassroomRepository.save(slotClassroom);
    }

    /**
     * Assigns a group of students to a specific slot-classroom combination for voting.
     * 
     * <p>This method batch-assigns multiple students to the same slot-classroom pairing,
     * effectively scheduling them for voting during the specified time slot in the
     * designated classroom. All student assignments are processed as a single batch
     * operation for optimal performance.
     * 
     * <p><strong>Performance Considerations:</strong>
     * Uses {@code saveAll()} for batch processing to minimize database round trips
     * when handling large student groups, improving overall allocation performance.
     * 
     * <p><strong>Database Impact:</strong>
     * Updates the {@code slot_classroom_id} field for all students in the list
     * and persists changes in a single transaction to maintain data consistency.
     * 
     * @param slotClassroom   the slot-classroom combination where students will vote.
     *                       Must not be null and should be a valid, previously saved entity
     *                       with assigned faculty supervision.
     * @param studentsToAllot the list of students to assign to this slot-classroom.
     *                       Must not be null but can be empty (no operation performed).
     *                       All students should be valid entities eligible for voting.
     * 
     * @author Ishaan Das
     */
    private void assignSlotClassroomsToStudents(SlotClassroom slotClassroom, List<Student> studentsToAllot) {
        for (Student student : studentsToAllot) {
            student.setSlotClassroom(slotClassroom);
        }
        studentRepository.saveAll(studentsToAllot);
    }

}