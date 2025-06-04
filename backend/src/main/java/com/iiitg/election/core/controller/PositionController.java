package com.iiitg.election.core.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iiitg.election.core.Position;
import com.iiitg.election.core.service.PositionService;

@RestController
@RequestMapping("/api/positions")
public class PositionController {
	
	@Autowired
	private PositionService positionService;
	
    @PostMapping("/create-new-position")
    @PreAuthorize("hasRole('ELECTION_MANAGER')")
    public ResponseEntity<Position> createPosition(@RequestBody Position position) {
        Position newPosition = positionService.createPosition(position.getPositionName());
        return new ResponseEntity<>(newPosition, HttpStatus.CREATED);
    }
    
    @PatchMapping("/{id}/edit-position")
    @PreAuthorize("hasRole('ELECTION_MANAGER')")
    public ResponseEntity<Position> editPosition(@PathVariable("id") String id, @RequestBody Position position) {
    	Position modifiedPosition = positionService.editPosition(id, position.getPositionName());
    	return new ResponseEntity<>(modifiedPosition, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}/delete-position")
    @PreAuthorize("hasRole('ELECTION_MANAGER')")
    public ResponseEntity<?> deletePosition(@PathVariable("id") String id) {
        positionService.deletePosition(id);
        return ResponseEntity.ok().body(Map.of("message", "Position deleted successfully"));
    }

}
