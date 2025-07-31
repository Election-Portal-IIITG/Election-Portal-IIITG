package com.iiitg.election.services.FileService;

import org.springframework.web.multipart.MultipartFile;

public interface FileProcessingService {
	public void processfile(MultipartFile file);
}
