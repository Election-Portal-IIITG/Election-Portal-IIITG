package com.iiitg.election.services.FileService;

import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iiitg.election.services.FileService.jpa.FileMetadataRepository;

@Service
public class FileServiceImpl implements FileService{
	
	@Autowired
	private FileMetadataRepository fileMetadataRepository;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileProcessingService fileProcessingService;

	@Override
	public String uploadFile(MultipartFile file, FileType type, String userId) {
		// TODO Auto-generated method stub
		
		String fieldId = UUID.randomUUID().toString();
		String storedName = generateStoredFilename(file.getOriginalFilename(), fieldId);
		
		String storagePath = fileStorageService.storeFile(file, storedName, type);
		
		FileMetadata metadata = FileMetadata.builder()
				.id(fieldId)
				.originalName(file.getOriginalFilename())
				.storedName(storedName)
				.fileType(type)
				.mimeType(file.getContentType())
				.fileSize(file.getSize())
				.storagePath(storagePath)
				.uploadedBy(userId)
				.isProcessed(false)
				.build();
		
		fileMetadataRepository.save(metadata);
		
		fileProcessingService.processfile(file);
		
		return "Uploaded successfully.....";
	}

	public FileMetadata getFileMetadata(String fileId) throws FileNotFoundException {
		Optional<FileMetadata> metadata = fileMetadataRepository.findById(fileId);
		
		if(metadata.isPresent()) {
			return metadata.get();
		}
		
		throw new FileNotFoundException("File not found with ID: " + fileId);
		
	}
	
	public void deleteMetadata(String fileId) {
		fileMetadataRepository.deleteById(fileId);
	}
	@Override
	public byte[] downloadFile(String fileId) throws FileNotFoundException  {
		// TODO Auto-generated method stub
		FileMetadata metadata = getFileMetadata(fileId);
		String storagePath = metadata.getStoragePath();
		
		return fileStorageService.retrieveFile(storagePath);
	}

	@Override
	public boolean deleteFile(String fileId) throws FileNotFoundException {
		// TODO Auto-generated method stub
		FileMetadata metadata = getFileMetadata(fileId);
		String storagePath = metadata.getStoragePath();
		
		fileStorageService.deleteFile(storagePath);
		
		deleteMetadata(fileId);
		
		return true;
	}
	
	private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
	
	private String generateStoredFilename(String originalFilename, String fileId) {
		String extension = getFileExtension(originalFilename);
        return fileId + "_" + System.currentTimeMillis() + "." + extension;
	}

}
