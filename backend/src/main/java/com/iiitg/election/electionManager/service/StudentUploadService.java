package com.iiitg.election.electionManager.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.iiitg.election.student.Student;
import com.iiitg.election.student.jpa.StudentSpringDataJpaRepository;

@Service
public class StudentUploadService {

    private static final Logger logger = LoggerFactory.getLogger(StudentUploadService.class);

    @Autowired
    private StudentSpringDataJpaRepository studentRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private SecurePasswordGenerator passwordGenerator; // Assuming this exists and is correct

    // If you want to attempt to parse the duplicate key info from the message
    // private static final Pattern MYSQL_DUPLICATE_KEY_PATTERN = Pattern.compile("Duplicate entry '(.+?)' for key");

    // Change @Transactional: We want default rollback behavior for RuntimeExceptions.
    // We will handle DIVEs for duplicates explicitly within the loop.
    @Async("voterProcessingExecutor")
    @Transactional // Remove noRollbackFor, let's handle it more explicitly
    public void processFileAndCreateStudentsAsync(Path filePath, String filename) {
        logger.info("Async processing STARTING for file: {} (Thread: {})", filename, Thread.currentThread().getName());
        logger.info("Is actual transaction active? {}", TransactionSynchronizationManager.isActualTransactionActive());


        int successCount = 0;
        int failureCount = 0;
        List<String> errorMessages = new ArrayList<>();
        boolean entireTransactionMarkedForRollback = false;

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader,
                     CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).setTrim(true).build())) {

            for (CSVRecord record : csvParser) {
                String rollNumberFromRecord = "N/A_IN_RECORD";
                String emailFromRecord = "N/A_IN_RECORD";

                try {
                    logger.debug("Processing record number: {}", record.getRecordNumber());
                    String name = record.get("name");
                    emailFromRecord = record.get("email");
                    rollNumberFromRecord = record.get("roll_number");
                    boolean onCampus = Boolean.parseBoolean(record.get("on_campus"));

                    if (name == null || name.trim().isEmpty() || emailFromRecord == null || emailFromRecord.trim().isEmpty()
                            || rollNumberFromRecord == null || rollNumberFromRecord.trim().isEmpty()) {
                        throw new IllegalArgumentException("Missing required data (name, email, or roll_number).");
                    }

                    // Pre-check for duplicates (good practice)
                    if (studentRepo.findByRollNumber(rollNumberFromRecord) != null || studentRepo.findByStudentEmailId(emailFromRecord) != null) {
                        String errorMsg = "Skipped (pre-check found existing): Roll " + rollNumberFromRecord + ", Email " + emailFromRecord;
                        logger.warn(errorMsg);
                        errorMessages.add(errorMsg);
                        failureCount++;
                        continue; // Skip this record
                    }

                    String[] names = name.split(" ", 2);
                    String firstName = names[0];
                    String lastName = (names.length > 1) ? names[1].trim() : "";

                    Student student = new Student();
                    student.setFirstName(firstName);
                    student.setLastName(lastName);
                    student.setStudentEmailId(emailFromRecord);
                    student.setRollNumber(rollNumberFromRecord);
                    student.setOnCampus(onCampus);
                    student.setHasVoted(false); // Default

                    String tempPassword = passwordGenerator.generatePassword(12); // Use a reasonable length
                    student.setPassword(encoder.encode(tempPassword));
                    // TODO: Securely communicate this tempPassword or use activation links

                    studentRepo.save(student);
                    successCount++;
                    logger.info("Successfully saved: Roll {}, Email {}", rollNumberFromRecord, emailFromRecord);

                } catch (IllegalArgumentException iae) {
                    String errorMsg = "Record " + record.getRecordNumber() + " (Roll " + rollNumberFromRecord + "): Invalid data - " + iae.getMessage();
                    logger.warn(errorMsg, iae);
                    errorMessages.add(errorMsg);
                    failureCount++;
                    // This is a data error for one record, continue with others.
                } catch (DataIntegrityViolationException dive) {
                    // This is likely from studentRepo.save() due to DB constraint
                    Throwable rootCause = dive.getMostSpecificCause(); // More reliable than getCause()
                    String logMessage;

                    if (rootCause instanceof SQLException) {
                        SQLException sqlEx = (SQLException) rootCause;
                        // MySQL: 1062, H2: 23505, PostgreSQL: 23505
                        // SQLState "23000" (integrity_constraint_violation) or "23505" (unique_violation)
                        if (sqlEx.getErrorCode() == 1062 || "23000".equals(sqlEx.getSQLState()) || "23505".equals(sqlEx.getSQLState())) {
                            logMessage = "Skipped (DB duplicate constraint): Roll " + rollNumberFromRecord + ", Email " + emailFromRecord + ". Details: " + sqlEx.getMessage();
                            logger.warn(logMessage);
                            errorMessages.add(logMessage);
                            failureCount++;
                            // This is a duplicate, continue with other records.
                        } else {
                            // Different SQL/Data integrity error - treat as critical for this batch
                            logMessage = "Record " + record.getRecordNumber() + " (Roll " + rollNumberFromRecord + "): Critical Data Integrity Error during save - " + dive.getMessage();
                            logger.error(logMessage, dive);
                            errorMessages.add(logMessage);
                            failureCount++;
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            entireTransactionMarkedForRollback = true; // Mark that we need to stop or indicate failure
                        }
                    } else {
                        // DIVE not caused by SQLException, or unexpected root cause. Treat as critical.
                        logMessage = "Record " + record.getRecordNumber() + " (Roll " + rollNumberFromRecord + "): Critical non-SQL Data Integrity Error during save - " + dive.getMessage();
                        logger.error(logMessage, dive);
                        errorMessages.add(logMessage);
                        failureCount++;
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        entireTransactionMarkedForRollback = true;
                    }
                } catch (Exception e) { // Catch any other unexpected error for this specific record
                    String errorMsgBase = "Record " + record.getRecordNumber() + " (Roll " + rollNumberFromRecord + ")";
                    logger.error(errorMsgBase + ": Unexpected critical error processing record - " + e.getMessage(), e);
                    errorMessages.add(errorMsgBase + ": Unexpected critical error - " + e.getMessage());
                    failureCount++;
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // Mark for rollback
                    entireTransactionMarkedForRollback = true;
                }

                if (entireTransactionMarkedForRollback) {
                     logger.error("A critical error occurred. Halting processing of further records in file {} and marking transaction for rollback.", filename);
                     // break; // Optional: stop processing further records in this file if one critical error occurs
                }
            } // End of for-loop over records

            if (TransactionAspectSupport.currentTransactionStatus().isRollbackOnly()) {
                logger.error("Async processing for file {} finished WITH CRITICAL ERRORS. Transaction will be rolled back. Attempted Success: {}, Failed: {}",
                        filename, successCount, failureCount);
                // Spring will handle the actual rollback due to the flag being set.
                // If we had `break;` in the loop for critical errors, successCount would be lower.
            } else {
                logger.info("Async processing for file {} finished. Success: {}, Failed (skipped duplicates/bad data): {}",
                        filename, successCount, failureCount);
            }

            if (!errorMessages.isEmpty()) {
                logger.warn("Summary of errors/skipped records for file {}:", filename);
                errorMessages.forEach(logger::warn);
            }

        } catch (IOException e) {
            logger.error("Fatal I/O error processing file {}: {}", filename, e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // Ensure rollback
            // No need to throw RuntimeException if @Async error handler is configured
            // or if void return type is acceptable on async failure.
            // throw new RuntimeException("Fatal I/O error processing file " + filename, e);
        } catch (Exception e) { // Catch-all for unexpected errors during setup (e.g., CSVParser init)
            logger.error("Outer uncaught exception during async processing of {}: {}", filename, e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // Ensure rollback
            // throw new RuntimeException("Outer uncaught exception for " + filename, e);
        } finally {
            try {
                Files.deleteIfExists(filePath);
                logger.info("Temporary file deleted: {}", filePath);
            } catch (IOException e) {
                logger.error("Error deleting temporary file {}: {}", filePath, e.getMessage());
            }
            logger.info("Async processing COMPLETED for file: {} (Thread: {})", filename, Thread.currentThread().getName());
        }
    }
}