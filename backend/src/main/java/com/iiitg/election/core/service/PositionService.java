package com.iiitg.election.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iiitg.election.core.Position;
import com.iiitg.election.core.jpa.PositionSpringDataJpaRepository;

@Service
public class PositionService {
	
	@Autowired
	private PositionSpringDataJpaRepository positionRepository;
	
	@Transactional
	public Position createPosition(String positionName) {
		Position newPosition = new Position(positionName);
		
		positionRepository.save(newPosition);
		
		return newPosition;
	}

	@Transactional
	public Position editPosition(String id, String newPositionName) {
		Position savedPosition = positionRepository.findById(id).get();

		savedPosition.setPositionName(newPositionName);
		
		positionRepository.save(savedPosition);
		
		return savedPosition;
		
	}

	@Transactional
	public void deletePosition(String id) {
		Position savedPosition = positionRepository.findById(id).get();
	    positionRepository.delete(savedPosition);
	}

	public Position getPosition(String contestingPositionName) {
		return positionRepository.findByPositionName(contestingPositionName);
	}
}
