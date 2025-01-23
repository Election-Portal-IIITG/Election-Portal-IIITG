package com.iiitg.election.clr;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.iiitg.election.allocation.Classroom;
import com.iiitg.election.allocation.FacultyClassroom;
import com.iiitg.election.allocation.StudentClassroom;
import com.iiitg.election.allocation.jpa.ClassroomSpringDataJpaRepository;
import com.iiitg.election.allocation.jpa.FacultyClassroomSpringDataJpaRepository;
import com.iiitg.election.allocation.jpa.StudentClassroomSpringDataJpaRepository;
import com.iiitg.election.faculty.Faculty;
import com.iiitg.election.faculty.jpa.FacultySpringDataJpaRepository;
import com.iiitg.election.student.Student;
import com.iiitg.election.student.jpa.StudentSpringDataJpaRepository;

@Component
public class JpaCLR implements CommandLineRunner{
	
	@Autowired
	private ClassroomSpringDataJpaRepository clsRepo;
	 
	@Autowired
	private FacultyClassroomSpringDataJpaRepository facClsRepo;
	
	@Autowired
	private StudentClassroomSpringDataJpaRepository studClsRepo;
	
	@Autowired
	private StudentSpringDataJpaRepository studRepo;
	
	@Autowired
	private FacultySpringDataJpaRepository facRepo;
	

	@Override
	public void run(String... args) throws Exception {
		Classroom clsrm = new Classroom("LG1", 70, true);
		clsRepo.save(clsrm);
		
		Student stu1 = new Student("abc@iiitg.ac.in","Ishaan","Das","2201097","DummyLoLOLOl",true,false,null);
		Student stu2 = new Student("xyz@iiitg.ac.in","Animesh","Das","2201024","DummyLoLOLOl",true,false,null);
		studRepo.save(stu1);
		studRepo.save(stu2);
		
		StudentClassroom alloc1 = new StudentClassroom(stu1,clsrm,LocalDateTime.now(),LocalDateTime.now().plusMinutes(15));
		StudentClassroom alloc2 = new StudentClassroom(stu2,clsrm,LocalDateTime.now(),LocalDateTime.now().plusMinutes(15));
		studClsRepo.save(alloc1);
		studClsRepo.save(alloc2);
		
		Faculty fac1 = new Faculty("sanjay@iiitg.ac.in","Sanjay","Moulik","DummyLolOLO",true,null);
		facRepo.save(fac1);
		
		FacultyClassroom facAloc1 = new FacultyClassroom(fac1,clsrm);
		facClsRepo.save(facAloc1);
	}

}
