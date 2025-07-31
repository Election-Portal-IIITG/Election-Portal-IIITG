package com.iiitg.election.services.FileService.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iiitg.election.services.FileService.FileMetadata;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, String> {

}
