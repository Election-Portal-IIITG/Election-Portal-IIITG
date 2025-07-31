package com.iiitg.election.services.FileService;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "file_metadata", indexes = {
	   @Index(name = "idx_uploaded_by", columnList = "uploadedBy"),
	   @Index(name = "idx_file_type", columnList = "fileType"),
	   @Index(name = "idx_upload_timestamp", columnList = "uploadTimestamp")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadata {
	
	@Id
	private String id;
	
	@Column(name = "original_name", nullable = false, length = 255)
	private String originalName;
	
	@Column(name = "stored_name", nullable = false, length = 255)
	private String storedName;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false, length = 10)
    private FileType fileType;
	
	@Column(name = "mime_type", nullable = false, length = 100)
    private String mimeType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "storage_path", nullable = false, length = 500)
    private String storagePath;
    
    @Column(name = "processed", nullable = false)
    private boolean isProcessed;

    @Column(name = "uploaded_by", nullable = false)
    private String uploadedBy;
    
    @Column(name = "upload_timestamp", nullable = false)
    private LocalDateTime uploadTimestamp;
    
    @PrePersist
    protected void onCreate() {
        if (uploadTimestamp == null) {
            uploadTimestamp = LocalDateTime.now();
        }
    }

}
