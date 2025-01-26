package com.iiitg.election.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.iiitg.election.electionManager.ElectionManager;
import com.iiitg.election.electionManager.ElectionManagerUser;
import com.iiitg.election.electionManager.jpa.ElectionManagerSpringDataJpaRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private ElectionManagerSpringDataJpaRepository electionManagerRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		ElectionManager manager = electionManagerRepo.findByManagerEmailId(username);
		
		if(manager == null) {
			System.out.println("Manager not found");
			throw new UsernameNotFoundException("User not found");
		}
		
		return new ElectionManagerUser(manager);
	}

}
