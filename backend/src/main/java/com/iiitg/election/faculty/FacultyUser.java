package com.iiitg.election.faculty;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class FacultyUser implements UserDetails {

	private Faculty faculty;

	public FacultyUser(Faculty faculty) {
		super();
		this.faculty = faculty;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return List.of(new SimpleGrantedAuthority("ROLE_FACULTY"));
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return faculty.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return faculty.getFacultyEmailId();
	}
	
	
	
}
