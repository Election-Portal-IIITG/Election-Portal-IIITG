package com.iiitg.election.services.FileService;

import java.io.FileNotFoundException;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	String uploadFile(MultipartFile file, FileType type, String userId);
	byte[] downloadFile(String fileId) throws FileNotFoundException;
	boolean deleteFile(String fileId) throws FileNotFoundException;
	
}
