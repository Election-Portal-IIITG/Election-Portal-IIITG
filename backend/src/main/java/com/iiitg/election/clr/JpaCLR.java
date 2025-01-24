package com.iiitg.election.clr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.iiitg.election.core.Position;
import com.iiitg.election.core.jpa.PositionSpringDataJpaRepository;
import com.iiitg.election.faculty.Faculty;
import com.iiitg.election.faculty.jpa.FacultySpringDataJpaRepository;
import com.iiitg.election.student.Candidate;
import com.iiitg.election.student.Student;
import com.iiitg.election.student.jpa.CandidateSpringDataJpaRepository;
import com.iiitg.election.student.jpa.StudentSpringDataJpaRepository;


@Component
public class JpaCLR implements CommandLineRunner{
	
	@Autowired
	private PositionSpringDataJpaRepository posRepo;
	
	@Autowired
	private StudentSpringDataJpaRepository stuRepo;
	
	@Autowired
	private CandidateSpringDataJpaRepository canRepo;
	
	@Autowired
	private FacultySpringDataJpaRepository facRepo;

	@Override
	public void run(String... args) throws Exception {
		
		Faculty fac1 = new Faculty("sanjay@iiitg.ac.in", "Sanjay", "Moulik", "DummyLoLoL", true);
		facRepo.save(fac1);
		
		Position presPos = new Position("President");
		Position vicePresPos = new Position("Vice President");
		
		posRepo.save(presPos);
		posRepo.save(vicePresPos);
		
		Student stu1 = new Student("abc@iiitg.ac.in", "Ishaan", "Das", "2201097" ,"DummyLoLOLOL", true, false);
		Student stu2 = new Student("xyz@iiitg.ac.in", "Animesh", "Kumar", "2201024" ,"DummyLoLOLOL", true, false);
		stuRepo.save(stu1);
		stuRepo.save(stu2);
		
		
		Candidate pres1 = new Candidate("B.Tech", 2026, "some url", "some about", "some manifesto url");
		Student foundStu = stuRepo.findByStudentEmailId("abc@iiitg.ac.in");
		Position foundPresPos = posRepo.findByPositionName("President");
		pres1.setStudent(foundStu);
		pres1.setContestingPosition(foundPresPos);
		canRepo.save(pres1);
		
		System.out.println(canRepo.findByContestingPosition_PositionName("President"));
	}
}
