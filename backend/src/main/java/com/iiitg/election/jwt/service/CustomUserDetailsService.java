package com.iiitg.election.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.iiitg.election.electionManager.ElectionManager;
import com.iiitg.election.electionManager.ElectionManagerUser;
import com.iiitg.election.electionManager.jpa.ElectionManagerSpringDataJpaRepository;
import com.iiitg.election.faculty.Faculty;
import com.iiitg.election.faculty.FacultyUser;
import com.iiitg.election.faculty.jpa.FacultySpringDataJpaRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private ElectionManagerSpringDataJpaRepository electionManagerRepo;
	
	@Autowired
	private FacultySpringDataJpaRepository facultyRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		ElectionManager manager = electionManagerRepo.findByManagerEmailId(username);
		
		if(manager != null) {
			return new ElectionManagerUser(manager);			
		}
		
		Faculty faculty = facultyRepo.findByFacultyEmailId(username);
		
		if(faculty != null) {
			return new FacultyUser(faculty);
		}
		System.out.println("Manager not found");
		throw new UsernameNotFoundException("User not found");

		
	}

}
