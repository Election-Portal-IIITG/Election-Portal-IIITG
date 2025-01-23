package com.iiitg.election.core.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iiitg.election.core.Position;

public interface PositionSpringDataJpaRepository extends JpaRepository<Position, String>{

}
