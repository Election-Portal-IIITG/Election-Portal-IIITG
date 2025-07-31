package com.iiitg.election.services.FileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iiitg.election.student.service.StudentAccountService;
import com.iiitg.election.student.service.StudentProcessingResult;

@Service
public class CSVProcessingService implements FileProcessingService {
	
	@Autowired
	private StudentAccountService studentAccountService;
	
	@Override
	public void processfile(MultipartFile file) {
		// TODO Auto-generated method stub
		
		System.err.println("Inside CSVProcessing");
		
		StudentProcessingResult result =  studentAccountService.createStudentsFromCSV(file);
	}

}
