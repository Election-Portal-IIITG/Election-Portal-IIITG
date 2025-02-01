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
import com.iiitg.election.allocation.service.AllocationService;
import com.iiitg.election.electionManager.ElectionManager;
import com.iiitg.election.electionManager.jpa.ElectionManagerSpringDataJpaRepository;
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
	
	@Autowired
	private ElectionManagerSpringDataJpaRepository manRepo;
	
	@Autowired
	private ElectionManagerSpringDataJpaRepository elecManRepo;
	
	@Autowired
	private AllocationService allocationService;
	

	@Override
	public void run(String... args) throws Exception {
		elecManRepo.save(new ElectionManager("def@iiitg.ac.in", "$2a$12$3PE8djUjNmXfhA1ysx6.tOjUfXOs0uhGuIFbkLf1lLsSuE8.Kdlme"));
		
		//Adding class
		Classroom lg1 = new Classroom("LG1", 3, true);
		Classroom lg2 = new Classroom("LG2", 2, true);
		Classroom c13 = new Classroom("C13", 1, true);
		Classroom c56 = new Classroom("C56", 1, false);
		
		clsRepo.save(lg1);
		clsRepo.save(lg2);
		clsRepo.save(c13);
		clsRepo.save(c56);
		
		
		//Creating time-stamps
		LocalDateTime votingStartTime = LocalDateTime.of(2025, 02, 05, 17, 0);
		int slotDuration = 15;
		int gapDuration = 20;
		
		
		//Creating Faculty
		Faculty f1 = new Faculty("matam@iiitg.ac.in", "Rakesh", "Matam", "DummyLOLOL", true);
		Faculty f2 = new Faculty("sanjay@iiitg.ac.in", "Sanjay", "Moulik", "DummyLOLOL", false);
		Faculty f3 = new Faculty("nilotpal@iiitg.ac.in", "Nilotpal", "Chakroborty", "DummyLOLOL", true);
		Faculty f4 = new Faculty("rohit@iiitg.ac.in", "Rohit", "Tripathi", "DummyLOLOL", true);
		Faculty f5 = new Faculty("sbnath@iiitg.ac.in", "SB", "Nath", "DummyLOLOL", true);
		
		//Saving Faculty
		facRepo.save(f1);
		facRepo.save(f2);
		facRepo.save(f3);
		facRepo.save(f4);
		facRepo.save(f5);
		
		
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
		
		allocationService.allocate(votingStartTime, slotDuration, gapDuration);
		

		ElectionManager man1 = new ElectionManager("sgc@iiitg.ac.in", "abc");
		manRepo.save(man1);
		
		System.out.println(manRepo.findByManagerEmailId("sgc@iiitg.ac.in"));
		
	}
}
