package com.iiitg.election.student.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iiitg.election.student.Student;
import com.iiitg.election.student.StudentCSVDto;
import com.iiitg.election.student.exceptions.FileProcessingException;
import com.iiitg.election.student.jpa.StudentSpringDataJpaRepository;
import com.iiitg.election.student.service.StudentProcessingResult.StudentValidationError;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
public class StudentAccountService {

	@Autowired
	private StudentSpringDataJpaRepository studentRepository;

	@Autowired
	private Validator validator;
	
	@Value("${app.student.batch-size:1000}")
    private int batchSize;
	
	@Transactional
	public StudentProcessingResult createStudentsFromCSV(MultipartFile file) {
		System.err.println("Creating Students from CSV - CALLLLLIIIIINNGGGGGGG");
		List<StudentCSVDto> csvData = parseCsvFile(file);
		
		System.err.println("Created Students from CSV - DOOOOOOONNNNNNEEEEEEEE");
		
		System.err.println("Starting processing of studentss ----->>>>>------>>>>>");
		StudentProcessingResult result = validateAndProcessStudents(csvData);
		
		System.err.println("Processed students OOOOOOOOOLLLLLLLLLAAAAAAAOOOOOOOOOOO");
		
		return result;
	}

	private List<StudentCSVDto> parseCsvFile(MultipartFile file) {
	    System.err.println("Inside parseCSV ?????????????????????????????????????");
	    List<StudentCSVDto> students = new ArrayList<>();

	    try (BufferedReader reader = new BufferedReader(
	            new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

	        CSVFormat csvFormat = CSVFormat.Builder.create()
	                .setHeader()
	                .setSkipHeaderRecord(true)
	                .setIgnoreHeaderCase(true)
	                .setTrim(true)
	                .build();

	        try (CSVParser csvParser = new CSVParser(reader, csvFormat)) {
	            int rowNumber = 1;

	            for (CSVRecord record : csvParser) {
	                rowNumber++;

	                try {
	                    StudentCSVDto student = StudentCSVDto.builder()
	                            .studentEmailId(record.get("email"))
	                            .firstName(record.get("firstName"))
	                            .lastName(record.get("lastName"))
	                            .rollNumber(record.get("rollNumber"))
	                            .onCampus(parseBoolean(record.get("onCampus")))
	                            .rowNumber(rowNumber)
	                            .build();

	                    students.add(student);
	                } catch (Exception ex) {
	                    students.add(StudentCSVDto.builder().rowNumber(rowNumber).build());
	                }
	            }
	        }

	    } catch (IOException ex) {
	        throw new FileProcessingException("Failed to read CSV file", ex);
	    }

	    return students;
	}


	private Boolean parseBoolean(String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}

		String trimmed = value.trim().toLowerCase();
		return switch (trimmed) {
		case "true", "True", "1", "yes", "y", "on" -> true;
		case "false", "False", "0", "no", "n", "off" -> false;
		default -> throw new IllegalArgumentException("Invalid boolean value: " + value);
		};
	}

	private StudentProcessingResult validateAndProcessStudents(List<StudentCSVDto> students) {
		System.err.println("Validating students ##########################################");
		List<StudentProcessingResult.StudentValidationError> errors = new ArrayList<>();
		List<Student> validStudents = new ArrayList<>();

		Set<String> existingEmails = studentRepository.findAllEmails();
		Set<String> existingRollNumbers = studentRepository.findAllRollNumbers();

		Set<String> batchEmails = new HashSet<>();
		Set<String> batchRollNumbers = new HashSet<>();

		for (StudentCSVDto dto : students) {
			List<String> validationErrors = new ArrayList<>();

			Set<ConstraintViolation<StudentCSVDto>> violations = validator.validate(dto);

			if (!violations.isEmpty()) {
				validationErrors
						.addAll(violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()));
			}

			if (dto.getStudentEmailId() != null) {
				if (existingEmails.contains(dto.getStudentEmailId().toLowerCase())) {
					validationErrors.add("Email already exists in database");
				}

				if (!batchEmails.add(dto.getStudentEmailId().toLowerCase())) {
					validationErrors.add("Duplicate email in current batch");
				}
			}

			if (dto.getRollNumber() != null) {
				if (existingRollNumbers.contains(dto.getRollNumber().toUpperCase())) {
					validationErrors.add("Roll number already exists in database");
				}

				if (!batchRollNumbers.add(dto.getRollNumber().toUpperCase())) {
					validationErrors.add("Duplicate roll number in current batch");
				}
			}

			if (!validationErrors.isEmpty()) {
				System.out.println("Validation error" + validationErrors);
				errors.add(StudentProcessingResult.StudentValidationError.builder().rowNumber(dto.getRowNumber())
						.rollNumber(dto.getRollNumber()).email(dto.getStudentEmailId())
						.validationErrors(validationErrors).errorType("VALIDATION").build());
			}
			else {
				try {
					Student student = dto.toStudent();
					validStudents.add(student);
				}
				catch (Exception ex) {
					System.err.println("Erroooooooooooooooooooooooooooooorrrr");
					errors.add(StudentProcessingResult.StudentValidationError.builder()
	                        .rowNumber(dto.getRowNumber())
	                        .rollNumber(dto.getRollNumber())
	                        .email(dto.getStudentEmailId())
	                        .validationErrors(List.of("Failed to create student entity: " + ex.getMessage()))
	                        .errorType("SYSTEM_ERROR")
	                        .build());
				}
			}
		}
		
		List<Student> savedStudents = new ArrayList<>();
        if (!validStudents.isEmpty()) {
            savedStudents = saveStudentsInBatches(validStudents, errors);
        }
        
        return StudentProcessingResult.builder()
            .totalProcessed(students.size())
            .successCount(savedStudents.size())
            .failureCount(errors.size())
            .errors(errors)
            .createdStudents(savedStudents)
            .build();
	}

	private List<Student> saveStudentsInBatches(List<Student> validStudents, List<StudentValidationError> errors) {
		// TODO Auto-generated method stub
		System.err.println("Saving in Batches---------------------------------------");
		List<Student> savedStudents = new ArrayList<>();
		for(int i = 0; i < validStudents.size(); i += batchSize) {
			int endIndex = Math.min(i + batchSize, validStudents.size());
			
			List<Student> batch = validStudents.subList(i, endIndex);
			
			try {
				List<Student> saved = studentRepository.saveAll(batch);
                savedStudents.addAll(saved);
                
                studentRepository.flush();
			}
			catch(Exception ex) {
				handleBatchSaveFailure(batch, errors, ex);
			}
		}
		return savedStudents;
	}

	private void handleBatchSaveFailure(List<Student> batch, List<StudentValidationError> errors, Exception batchException) {
		// TODO Auto-generated method stub
		
		for (Student student : batch) {
            try {
                studentRepository.save(student);
                
            } catch (Exception ex) {
                errors.add(StudentProcessingResult.StudentValidationError.builder()
                    .rollNumber(student.getRollNumber())
                    .email(student.getStudentEmailId())
                    .validationErrors(List.of("Database save failed: " + ex.getMessage()))
                    .errorType("SYSTEM_ERROR")
                    .build());
            }
            
        }
		
	}

}
