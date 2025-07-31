package com.iiitg.election.services.FileService;

import java.io.FileNotFoundException;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
	String storeFile(MultipartFile file, String storedName, FileType type);
    byte[] retrieveFile(String storagePath) throws FileNotFoundException;
    void deleteFile(String storagePath);
    String generatePublicUrl(String storagePath);
}
