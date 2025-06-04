package com.iiitg.election.clr;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.iiitg.election.allocation.Classroom;
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



//@Component
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
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	

	@Override
	public void run(String... args) throws Exception {
//		elecManRepo.save(new ElectionManager("def@iiitg.ac.in", "$2a$12$3PE8djUjNmXfhA1ysx6.tOjUfXOs0uhGuIFbkLf1lLsSuE8.Kdlme"));
//		
//	    // Save 9 Classrooms with varying capacity and availability
//	    clsRepo.save(new Classroom("CR1", 10, true));
//	    clsRepo.save(new Classroom("CR2", 10, false));
//	    clsRepo.save(new Classroom("CR3", 8, false));
//	    clsRepo.save(new Classroom("CR4", 7, false));
//	    clsRepo.save(new Classroom("CR5", 5, false));
//	    clsRepo.save(new Classroom("CR6", 4, true));
//	    clsRepo.save(new Classroom("CR7", 3, true));
//	    clsRepo.save(new Classroom("CR8", 2, true));
//	    clsRepo.save(new Classroom("CR9", 1, false));
//
//	    // Save 7 available faculties
//	    facRepo.save(new Faculty("f1@iiitg.ac.in", "First", "Faculty", "Dummy", true));
//	    facRepo.save(new Faculty("f2@iiitg.ac.in", "Second", "Faculty", "Dummy", true));
//	    facRepo.save(new Faculty("f3@iiitg.ac.in", "Third", "Faculty", "Dummy", true));
//	    facRepo.save(new Faculty("f4@iiitg.ac.in", "Fourth", "Faculty", "Dummy", true));
//	    facRepo.save(new Faculty("f5@iiitg.ac.in", "Fifth", "Faculty", "Dummy", true));
//	    facRepo.save(new Faculty("f6@iiitg.ac.in", "Sixth", "Faculty", "Dummy", true));
//	    facRepo.save(new Faculty("f7@iiitg.ac.in", "Seventh", "Faculty", "Dummy", true));
//
//	    // Save 30 on-campus students with roll numbers 2201001 to 2201030
//	    for (int i = 1; i <= 30; i++) {
//	        String roll = String.format("22010%02d", i);
//	        String email = "stu" + i + "@iiitg.ac.in";
//	        stuRepo.save(new Student(email, "Student" + i, "Last", roll, "Dummy"));
//	    }
//
//	    // Time and duration settings
//	    LocalDateTime votingStartTime = LocalDateTime.of(2025, 2, 5, 17, 0);
//	    int slotDuration = 15;
//	    int gapDuration = 10;
//
//	    // Allocation logic
//	    allocationService.allocate(votingStartTime, slotDuration, gapDuration);
//		
//
//		ElectionManager man1 = new ElectionManager("sgc@iiitg.ac.in", "abc");
//		manRepo.save(man1);
//		
//		System.out.println(manRepo.findByManagerEmailId("sgc@iiitg.ac.in"));
		
		Student stu = new Student("ishaan.das22b@iiitg.ac.in", "Ishaan", "Das", "2201097", encoder.encode("SecPass123"));
		stuRepo.save(stu);
	}
}
