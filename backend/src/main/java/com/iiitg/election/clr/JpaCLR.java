package com.iiitg.election.clr;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.iiitg.election.core.Election;
import com.iiitg.election.core.Position;
import com.iiitg.election.core.Result;
import com.iiitg.election.core.Winner;
import com.iiitg.election.core.jpa.ElectionSpringDataJpaRepository;
import com.iiitg.election.core.jpa.PositionSpringDataJpaRepository;
import com.iiitg.election.core.jpa.ResultSpringDataJpaRepository;
import com.iiitg.election.core.jpa.WinnerSpringDataJpaRepository;
import com.iiitg.election.electionManager.ElectionManager;
import com.iiitg.election.electionManager.jpa.ElectionManagerSpringDataJpaRepository;


@Component
public class JpaCLR implements CommandLineRunner{
	
	@Autowired
	private ElectionSpringDataJpaRepository elecRepo;
	
	@Autowired
	private ResultSpringDataJpaRepository resRepo;
	
	@Autowired
	private PositionSpringDataJpaRepository posRepo;
	
	@Autowired
	private WinnerSpringDataJpaRepository winRepo;
	
	@Autowired
	private ElectionManagerSpringDataJpaRepository manRepo;
	

	@Override
	public void run(String... args) throws Exception {
		LocalDate date = LocalDate.now();
		Election newElec = new Election(date, false);
		elecRepo.save(newElec);
		
		Position president = new Position("President");
		posRepo.save(president);
		
		Result newResult = new Result("2201097", "Ishaan Das", 690);
		newResult.setElection(newElec);
		newResult.setPosition(president);
		resRepo.save(newResult);
		
//		List<Result> all = resRepo.findByPosition_PositionName("President");
//		List<Result> all = resRepo.findByElectionYear(2025);
		List<Result> all = resRepo.findByElectionYearAndPositionName(2025, "Vice President");
		
		Winner newWinner = new Winner("abc@iiitg.ac.in", "2201097", "Ishaan Das", "Some Img Url", "B.Tech", president);
		winRepo.save(newWinner);
		
		System.out.println(resRepo.findByElectionYearAndPositionName(2025, "President"));
		
		ElectionManager man1 = new ElectionManager("sgc@iiitg.ac.in", "abc");
		manRepo.save(man1);
		
		System.out.println(manRepo.findByManagerEmailId("sgc@iiitg.ac.in"));
		
	}
}
