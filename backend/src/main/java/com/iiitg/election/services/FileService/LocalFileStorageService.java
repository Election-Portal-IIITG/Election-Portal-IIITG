package com.iiitg.election.services.FileService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iiitg.election.services.FileService.Exceptions.FileStorageException;

import jakarta.annotation.PostConstruct;

@Service
public class LocalFileStorageService implements FileStorageService {

	@Value("${file.upload.local-path:./uploads}")
	private String uploadPath;
	
	@PostConstruct
	public void init() {
		try {
			Files.createDirectories(Paths.get(uploadPath));
		}
		catch (IOException ex) {
			throw new RuntimeException("Could not create upload directory", ex);
		}
	}
	@Override
	public String storeFile(MultipartFile file, String storedName, FileType type) {
		try {
			Path typeDirectory = Paths.get(uploadPath, type.name().toLowerCase());
			Files.createDirectories(typeDirectory);
			
			Path filePath = typeDirectory.resolve(storedName);
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			
			return filePath.toString();
		}
		catch (IOException ex) {
			throw new FileStorageException("Failed to store file: " + storedName, ex);
		}
	}

	@Override
	public byte[] retrieveFile(String storagePath) throws FileNotFoundException {
		try {
			Path filePath = Paths.get(storagePath);
			
			if(!Files.exists(filePath)) {
				throw new FileNotFoundException("File not found: " + storagePath);
			}
			
			return Files.readAllBytes(filePath);
		}
		catch (FileNotFoundException ex) {
			throw ex;
		}
		catch (IOException ex) {
			throw new FileStorageException("Failed to retrieve file: " + storagePath, ex);
		}
	}

	@Override
	public void deleteFile(String storagePath) {
		try {
			Path filePath = Paths.get(storagePath);
			
			Files.deleteIfExists(filePath);
		}
		catch (IOException ex) {
            throw new FileStorageException("Failed to delete file: " + storagePath, ex);
        }
	}
	@Override
	public String generatePublicUrl(String storagePath) {
		return "/api/files/download/" + Paths.get(storagePath).getFileName().toString();
	}
	
	

}
