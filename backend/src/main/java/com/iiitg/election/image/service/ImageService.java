package com.iiitg.election.image.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
	private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
	
    private static final String UPLOAD_BASE_DIR = "uploads";
    private static final String CANDIDATE_IMAGE_DIR = "candidate-image";
    
    // Maximum file size (5MB)
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    
    // Allowed image formats
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png");
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
        "image/jpeg", "image/jpg", "image/png"
    );
    
    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;
    
    
    public String uploadCandidateImage(MultipartFile file, String studentId) throws IOException {
        // Validate file
        validateFile(file);
        
        // Create directory if it doesn't exist
        String uploadDir = UPLOAD_BASE_DIR + File.separator + CANDIDATE_IMAGE_DIR;
        Path uploadPath = Paths.get(uploadDir);
        
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            logger.info("Created directory: {}", uploadPath.toAbsolutePath());
        }
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String uniqueFilename = generateUniqueFilename(studentId, extension);
        
        // Save file
        Path filePath = uploadPath.resolve(uniqueFilename);
        
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("File uploaded successfully: {}", filePath.toAbsolutePath());
            
            // Return the URL
            return generateFileUrl(uniqueFilename);
            
        } catch (IOException e) {
            logger.error("Failed to upload file: {}", e.getMessage());
            throw new IOException("Failed to upload file: " + e.getMessage());
        }
    }
    
    private void validateFile(MultipartFile file) {
        // Check if file is empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }
        
        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size cannot exceed 5MB");
        }
        
        // Check file extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be empty");
        }
        
        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Only image files (jpg, jpeg, png) are allowed");
        }
        
        // Check MIME type
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Invalid file type. Only image files are allowed");
        }
    }
    
    public boolean deleteImage(String imageUrl) {
        try {
            // Extract the filename from the URL
            String[] parts = imageUrl.split("/");
            String filename = parts[parts.length - 1];

            // Build the path to the file
            String filePathStr = UPLOAD_BASE_DIR + File.separator + CANDIDATE_IMAGE_DIR + File.separator + filename;
            Path filePath = Paths.get(filePathStr);

            // Delete the file if it exists
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                logger.info("Deleted image file: {}", filePath.toAbsolutePath());
                return true;
            } else {
                logger.warn("Image file not found for deletion: {}", filePath.toAbsolutePath());
                return false;
            }
        } catch (Exception e) {
            logger.error("Failed to delete image: {}", e.getMessage());
            return false;
        }
    }

    
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
    
    private String generateUniqueFilename(String studentId, String extension) {
        // Generate filename: candidate_studentId_timestamp.extension
        String timestamp = String.valueOf(System.currentTimeMillis());
        return String.format("candidate_%s_%s.%s", studentId, timestamp, extension);
    }
    
    private String generateFileUrl(String filename) {
        return String.format("%s/%s/%s/%s", baseUrl, UPLOAD_BASE_DIR, CANDIDATE_IMAGE_DIR, filename);
    }
}