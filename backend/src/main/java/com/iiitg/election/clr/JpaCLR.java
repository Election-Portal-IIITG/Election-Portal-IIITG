package com.iiitg.election.clr;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.iiitg.election.allocation.Classroom;
import com.iiitg.election.allocation.Slot;
import com.iiitg.election.allocation.SlotClassroom;
import com.iiitg.election.allocation.jpa.ClassroomSpringDataJpaRepository;
import com.iiitg.election.allocation.jpa.SlotClassroomSpringDataJpaRepository;
import com.iiitg.election.allocation.jpa.SlotSpringDataJpaRepository;
import com.iiitg.election.faculty.Faculty;
import com.iiitg.election.faculty.jpa.FacultySpringDataJpaRepository;
import com.iiitg.election.student.Student;
import com.iiitg.election.student.jpa.StudentSpringDataJpaRepository;


@Component
public class JpaCLR implements CommandLineRunner{
	
	@Autowired
	private ClassroomSpringDataJpaRepository clsRepo;
	
	@Autowired
	private SlotSpringDataJpaRepository slotRepo;
	
	@Autowired
	private SlotClassroomSpringDataJpaRepository slotClsRepo;
	
	@Autowired
	private FacultySpringDataJpaRepository facRepo;
	
	@Autowired
	private StudentSpringDataJpaRepository stuRepo;
	

	@Override
	public void run(String... args) throws Exception {
		//Adding class
		Classroom lg1 = new Classroom("LG1", 120, true);
		Classroom lg2 = new Classroom("LG2", 120, true);
		Classroom c13 = new Classroom("C13", 70, true);
		
		clsRepo.save(lg1);
		clsRepo.save(lg2);
		clsRepo.save(c13);
		
		
		//Creating time-stamps
		LocalDateTime s1Start = LocalDateTime.of(2025, 01, 12, 5, 0);
		LocalDateTime s1End = LocalDateTime.of(2025, 01, 12, 5, 15);
		
		LocalDateTime s2Start = LocalDateTime.of(2025, 01, 12, 6, 30);
		LocalDateTime s2End = LocalDateTime.of(2025, 01, 12, 6, 45);	
		
		
		//Adding slots
		Slot s1 = new Slot(s1Start, s1End);
		Slot s2 = new Slot(s2Start, s2End);
		slotRepo.save(s1);
		slotRepo.save(s2);
		
		//Mapping slots and classroom
		SlotClassroom lg1S1 = new SlotClassroom(lg1, s1);
		SlotClassroom lg1S2 = new SlotClassroom(lg1, s2);
		SlotClassroom lg2S1 = new SlotClassroom(lg2, s1);
		SlotClassroom c13S1 = new SlotClassroom(c13, s1);
		
		slotClsRepo.save(lg1S1);
		slotClsRepo.save(lg1S2);
		slotClsRepo.save(lg2S1);
		slotClsRepo.save(c13S1);
		
		
		//Creating Faculty
		Faculty f1 = new Faculty("matam@iiitg.ac.in", "Rakesh", "Matam", "DummyLOLOL", true);
		Faculty f2 = new Faculty("sanjay@iiitg.ac.in", "Sanjay", "Moulik", "DummyLOLOL", true);
		Faculty f3 = new Faculty("nilotpal@iiitg.ac.in", "Nilotpal", "Chakroborty", "DummyLOLOL", true);
		
		//Saving Faculty
		facRepo.save(f1);
		facRepo.save(f2);
		facRepo.save(f3);
		
		
		//Creating student
		Student stu1 = new Student("a@iiitg.ac.in", "Ishaan", "Das", "2201097", "DummyLOLOL", true, false);
		Student stu2 = new Student("ab@iiitg.ac.in", "Animesh", "Kumar", "2201024", "DummyLOLOL", true, false);
		Student stu3 = new Student("abc@iiitg.ac.in", "Arnav", "Raj", "2201031", "DummyLOLOL", true, false);
		Student stu4 = new Student("abcd@iiitg.ac.in", "Piyush", "Upadhyay", "2201180", "DummyLOLOL", true, false);
		
		//saving student
		stuRepo.save(stu1);
		stuRepo.save(stu2);
		stuRepo.save(stu3);
		stuRepo.save(stu4);
		
		//assigning cls_slot to students
		stu1.setSlotClassroom(lg1S1);
		stu2.setSlotClassroom(lg1S2);
		stu3.setSlotClassroom(lg2S1);
		stu4.setSlotClassroom(c13S1);
		stuRepo.save(stu1);
		stuRepo.save(stu2);
		stuRepo.save(stu3);
		stuRepo.save(stu4);
		
		//assigning faculty to classroom
		lg1.setAssignedFaculty(f1);
		lg2.setAssignedFaculty(f2);
		c13.setAssignedFaculty(f3);
		clsRepo.save(lg1);
		clsRepo.save(lg2);
		clsRepo.save(c13);
		
		List<Student> allInLg1 = stuRepo.findBySlotClassroom_Classroom_ClassroomName("LG1");
		System.out.println(allInLg1);
		
		List<Student> allInLg1S1 = stuRepo.findBySlotClassroom_Classroom_ClassroomNameAndSlotClassroom_Slot("LG1", s1);
		System.out.println(allInLg1S1);
		
		List<Student> allInS1 = stuRepo.findBySlotClassroom_Slot(s1);
		System.out.println(allInS1);
		
		Faculty newF = facRepo.findByAssignedClassroom_ClassroomName("LG2");
		System.out.println(newF);
		
		List<Faculty> allBusyFacultyInSlot = facRepo.findByAssignedClassroom_ClassroomSlots_Slot(s1);
		System.out.println(allBusyFacultyInSlot);
		
		List<Slot> allSlotOfParticularClassroom = slotRepo.findByClassroomSlots_Classroom_ClassroomName("LG2");
		System.out.println(allSlotOfParticularClassroom);
		
		List<Slot> allSlotOfParticularFaculty = slotRepo.findByClassroomSlots_Classroom_AssignedFaculty_FacultyEmailId("matam@iiitg.ac.in");
		System.out.println(allSlotOfParticularFaculty);
		
		List<SlotClassroom> allSlotClassOfParticularFaculty = slotClsRepo.findByClassroom_AssignedFaculty_FacultyEmailId("matam@iiitg.ac.in");
		System.out.println(allSlotClassOfParticularFaculty);
		
		SlotClassroom slotClsOfParticularStudent = slotClsRepo.findByStudentEmailId("abcd@iiitg.ac.in");
		System.out.println(slotClsOfParticularStudent);
	}
}
