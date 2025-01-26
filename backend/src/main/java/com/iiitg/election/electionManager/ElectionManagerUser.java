package com.iiitg.election.electionManager;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class ElectionManagerUser implements UserDetails {
	
	private ElectionManager manager;
	
	public ElectionManagerUser(ElectionManager manager) {
		super();
		this.manager = manager;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return List.of(new SimpleGrantedAuthority("ADMIN"));
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return manager.getPassword(); 
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return manager.getManagerEmailId();
	}

}
