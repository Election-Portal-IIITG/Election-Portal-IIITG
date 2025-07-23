package com.iiitg.election.student.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iiitg.election.core.Position;
import com.iiitg.election.core.service.PositionService;
import com.iiitg.election.core.utils.TokenHashUtil;
import com.iiitg.election.faculty.Faculty;
import com.iiitg.election.faculty.dto.ApproverDTO;
import com.iiitg.election.faculty.jpa.FacultySpringDataJpaRepository;
import com.iiitg.election.image.service.ImageService;
import com.iiitg.election.jwt.JwtService;
import com.iiitg.election.jwt.TokenData;
import com.iiitg.election.services.EmailService.EmailRequest;
import com.iiitg.election.services.EmailService.EmailService;
import com.iiitg.election.services.EmailService.EmailType;
import com.iiitg.election.student.Candidate;
import com.iiitg.election.student.Student;
import com.iiitg.election.student.controller.CandidateRegistrationDto;
import com.iiitg.election.student.dto.ApprovedCandidateDTO;
import com.iiitg.election.student.dto.NominatorDTO;
import com.iiitg.election.student.exceptions.ApprovalException;
import com.iiitg.election.student.exceptions.BusinessValidationException;
import com.iiitg.election.student.exceptions.InvalidTokenException;
import com.iiitg.election.student.exceptions.NominationException;
import com.iiitg.election.student.jpa.CandidateSpringDataJpaRepository;
import com.iiitg.election.student.jpa.StudentSpringDataJpaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CandidateService {
	
	private static final Set<String> VALID_PROGRAMMES = Set.of("B.Tech", "M.Tech", "PhD");
	
    @Autowired
    private CandidateSpringDataJpaRepository candidateRepository;
    
    @Autowired
    private StudentSpringDataJpaRepository studentRepository;
    
    @Autowired
    private FacultySpringDataJpaRepository facultyRepository;
    
    @Autowired
    private PositionService positionService;
    
    @Autowired
    private ImageService imageService;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private TokenHashUtil tokenHashUtil;
    
    @Autowired
    private EmailService emailService;
    
    public Candidate registerCandidate(String studentEmailId, CandidateRegistrationDto candidateRequest) throws IOException {

        // Find student by email
        Student student = studentRepository.findByStudentEmailId(studentEmailId);
        if (student == null) {
            throw new EntityNotFoundException("Student not found with email: " + studentEmailId);
        }
        

        // Check if student already has a candidate
        if (student.getCandidate() != null) {
            throw new DataIntegrityViolationException("Student is already registered as a candidate");
        }
        
        //Find Position by Position Name
        Position contestingPosition = positionService.getPosition(candidateRequest.getContestingPositionName());
        if (contestingPosition == null) {
        	throw new EntityNotFoundException("Position not found with name: " + candidateRequest.getContestingPositionName());
        }
        
        //Check if Programme is valid
        if(!VALID_PROGRAMMES.contains(candidateRequest.getProgramme())) {
        	throw new IllegalArgumentException(candidateRequest.getProgramme() + " is an INVALID PROGRAMME");
        }
        
        
        MultipartFile candidateImage = candidateRequest.getProfileImage();
        
        String imageURL = imageService.uploadCandidateImage(candidateImage, student.getId());

        // Create candidate object
        Candidate candidate = new Candidate(
            candidateRequest.getProgramme(),
            candidateRequest.getGraduatingYear(),
            imageURL,
            candidateRequest.getAbout(),
            contestingPosition
        );

        candidate.setStudent(student);

        try {
            // Save candidate
            return candidateRepository.save(candidate);
        } catch (Exception ex) {
            // Delete uploaded image if candidate save fails
        	imageService.deleteImage(candidate.getStudentImageURL()); // Make sure this method exists
            throw new RuntimeException("Candidate registration failed. Image deleted.", ex);
        }
    }
    
    public String requestNomination(String candidateEmailId, String nominatorEmailId) {
        Candidate candidate = candidateRepository.findByStudent_StudentEmailId(candidateEmailId);
        if (candidate == null) throw new EntityNotFoundException("Candidate not found");

        if (Boolean.TRUE.equals(candidate.getIsNominated())) {
            throw new NominationException("Already nominated by another student");
        }

        Student nominator = studentRepository.findByStudentEmailId(nominatorEmailId);
        if (nominator == null) throw new EntityNotFoundException("Student not found");

        if (candidateEmailId.equals(nominatorEmailId)) {
            throw new NominationException("Cannot nominate yourself");
        }

        // Generate and store hash of the token
        String token = jwtService.generateNominationToken(candidateEmailId, nominatorEmailId);
        String tokenHash = tokenHashUtil.hashToken(token);

        candidate.setNominatedBy(nominator);
        candidate.setIsNominated(null);
        candidate.setNominationTokenHash(tokenHash);
        candidateRepository.save(candidate);

        // Prepare email links
        String acceptLink = "http://localhost:8080/api/candidates/nomination-response?token=" + token + "&accept=true";
        String rejectLink = "http://localhost:8080/api/candidates/nomination-response?token=" + token + "&accept=false";

        // Prepare the email
        Map<String, Object> model = new HashMap<>();
        model.put("nominatorName", nominator.getFirstName());
        model.put("candidateName", candidate.getStudent().getFirstName());
        model.put("acceptLink", acceptLink);
        model.put("rejectLink", rejectLink);

        EmailRequest emailRequest = EmailRequest.builder()
            .to(nominator.getStudentEmailId())
            .subject("Nomination Request from " + candidate.getStudent().getFirstName())
            .emailType(EmailType.NOMINATION_REQUEST)
            .templateModel(model)
            .build();

        emailService.sendEmailAsync(emailRequest); // or sendEmail(...) if you want it sync

        return "Nomination request sent to " + nominator.getFirstName();
    }

    
    public String handleNominationResponse(String token, boolean accept) {
        // Validate JWT token first
        TokenData tokenData = jwtService.validateActionToken(token);
        
        if (!tokenData.getType().equals("NOMINATION")) {
            throw new InvalidTokenException("Invalid nomination token");
        }
        
        Candidate candidate = candidateRepository.findByStudent_StudentEmailId(tokenData.getCandidateEmailId());
        if(candidate == null) {
            throw new EntityNotFoundException("Candidate not found");
        }
        
        // Check if the token hash matches the stored hash
        if (candidate.getNominationTokenHash() == null || 
            !tokenHashUtil.verifyToken(token, candidate.getNominationTokenHash())) {
            throw new InvalidTokenException("Invalid or expired nomination token. This nomination request may have been superseded by a newer one.");
        }
        
        // Check if already processed (additional safety check)
        if (candidate.getIsNominated() != null) {
            throw new NominationException("This nomination has already been processed");
        }
        
        // Update nomination status and clear the token hash
        candidate.setIsNominated(accept);
        candidate.setNominationTokenHash(null); // Clear the hash after processing
        
        // If rejected, also clear the nominator so they can request from someone else
        if (!accept) {
            candidate.setNominatedBy(null);
        }
        
        candidateRepository.save(candidate);
        
        return accept ? 
            "Nomination accepted successfully! " + candidate.getStudent().getFirstName() + " is now nominated." : 
            "Nomination rejected. The candidate can now request nomination from another student.";
    }

    public String requestApproval(String candidateEmailId, String facultyEmailId) {
        Candidate candidate = candidateRepository.findByStudent_StudentEmailId(candidateEmailId);
        if(candidate == null) throw new EntityNotFoundException("Candidate not found");

        if (candidate.getIsNominated() == null)
            throw new ApprovalException("Cannot request approval: Candidate nomination is still pending");
        if (!candidate.getIsNominated())
            throw new ApprovalException("Cannot request approval: Candidate is not nominated by any student");

        if (Boolean.TRUE.equals(candidate.getIsApproved()))
            throw new ApprovalException("Candidate is already approved by faculty");

        Faculty faculty = facultyRepository.findByFacultyEmailId(facultyEmailId);
        if(faculty == null) throw new EntityNotFoundException("Faculty not found");

        // Token generation
        String token = jwtService.generateApprovalToken(candidateEmailId, facultyEmailId);
        String tokenHash = tokenHashUtil.hashToken(token);

        candidate.setApprovedBy(faculty);
        candidate.setIsApproved(null);
        candidate.setApprovalTokenHash(tokenHash);
        candidateRepository.save(candidate);

        String acceptLink = "http://localhost:8080/api/candidates/approval-response?token=" + token + "&approve=true";
        String rejectLink = "http://localhost:8080/api/candidates/approval-response?token=" + token + "&approve=false";

        // Email model
        Map<String, Object> model = new HashMap<>();
        model.put("facultyName", faculty.getFirstName());
        model.put("candidateName", candidate.getStudent().getFirstName() + " " + candidate.getStudent().getLastName());
        model.put("positionName", candidate.getContestingPosition().getPositionName());
        model.put("nominatorName", candidate.getNominatedBy().getFirstName() + " " + candidate.getNominatedBy().getLastName());
        model.put("approveLink", acceptLink);
        model.put("rejectLink", rejectLink);

        EmailRequest emailRequest = EmailRequest.builder()
            .to(faculty.getFacultyEmailId())
            .subject("Approval Request for " + candidate.getStudent().getFirstName())
            .emailType(EmailType.APPROVAL_REQUEST)
            .templateModel(model)
            .build();

        emailService.sendEmailAsync(emailRequest);

        return "Approval request sent to " + faculty.getFirstName() + " (" + faculty.getFacultyEmailId() + ")";
    }
    
    public String handleApprovalResponse(String token, boolean approve) {
        // Validate JWT token first
        TokenData tokenData = jwtService.validateActionToken(token);
        
        if (!tokenData.getType().equals("APPROVAL")) {
            throw new InvalidTokenException("Invalid approval token");
        }
        
        Candidate candidate = candidateRepository.findByStudent_StudentEmailId(tokenData.getCandidateEmailId());
        if(candidate == null) {
            throw new EntityNotFoundException("Candidate not found");
        }
        
        // Check if the token hash matches the stored hash
        if (candidate.getApprovalTokenHash() == null || 
            !tokenHashUtil.verifyToken(token, candidate.getApprovalTokenHash())) {
            throw new InvalidTokenException("Invalid or expired approval token. This approval request may have been superseded by a newer one.");
        }
        
        // Check if already processed (additional safety check)
        if (candidate.getIsApproved() != null) {
            throw new ApprovalException("This approval request has already been processed");
        }
        
        // Verify candidate is still nominated
        if (candidate.getIsNominated() == null || !candidate.getIsNominated()) {
            throw new ApprovalException("Cannot process approval: Candidate is no longer nominated");
        }
        
        // Update approval status and clear the token hash
        candidate.setIsApproved(approve);
        candidate.setApprovalTokenHash(null); // Clear the hash after processing
        
        // If rejected, clear the approver so they can request from another faculty
        if (!approve) {
            candidate.setApprovedBy(null);
        }
        
        candidateRepository.save(candidate);
        
        String candidateName = candidate.getStudent().getFirstName() + " " + candidate.getStudent().getLastName();
        String position = candidate.getContestingPosition().getPositionName();
        
        return approve ? 
            "Approval granted successfully! " + candidateName + " is now approved to contest for " + position + "." : 
            "Approval rejected. " + candidateName + " can request approval from another faculty member.";
    }
    
	public List<ApprovedCandidateDTO> getAllApprovedCandidates() {
	    // Find all candidates where isApproved = true
	    List<Candidate> approvedCandidates = candidateRepository.findByIsApprovedTrue();
	    
	    if (approvedCandidates.isEmpty()) {
	        throw new EntityNotFoundException("No approved candidates found");
	    }
	    
	    return approvedCandidates.stream()
	        .map(this::convertToApprovedCandidateDTO)
	        .collect(Collectors.toList());
	}
	
	private ApprovedCandidateDTO convertToApprovedCandidateDTO(Candidate candidate) {
	    ApprovedCandidateDTO dto = new ApprovedCandidateDTO();
	    
	    // Basic candidate info from student
	    Student student = candidate.getStudent();
	    dto.setStudentEmailId(student.getStudentEmailId());
	    dto.setFirstName(student.getFirstName());
	    dto.setLastName(student.getLastName());
	    dto.setRollNumber(student.getRollNumber());
	    
	    // Candidate specific info
	    dto.setProgramme(candidate.getProgramme());
	    dto.setGraduatingYear(candidate.getGraduatingYear());
	    dto.setStudentImageURL(candidate.getStudentImageURL()); // Frontend will use this URL
	    dto.setIsEligible(candidate.getIsEligible());
	    
	    // Nominator info
	    Student nominator = candidate.getNominatedBy();
	    NominatorDTO nominatorDTO = new NominatorDTO(nominator.getFirstName(),
	    		nominator.getLastName(),
	    		nominator.getRollNumber(),
	    		nominator.getStudentEmailId()
	    		);
	    dto.setNominatedBy(nominatorDTO);
	    
	    // Approver info
	    Faculty approver = candidate.getApprovedBy();
	        ApproverDTO approverDTO = new ApproverDTO(
	            approver.getFirstName(),
	            approver.getLastName(),
	            approver.getFacultyEmailId()
	        );
	    dto.setApprovedBy(approverDTO);
	    
	    // Position info
	    Position position = candidate.getContestingPosition();
	    dto.setContestingPosition(position.getPositionName());
	    
	    return dto;
	}
	
	public void updateCandidateEligibility(String candidateEmailId, Boolean isEligible) {
	    Candidate candidate = candidateRepository.findByStudent_StudentEmailId(candidateEmailId);
	    if (candidate == null) {
	        throw new EntityNotFoundException("Candidate not found with email: " + candidateEmailId);
	    }
	    
	    // Check if candidate is approved - only approved candidates can have eligibility updated
	    if (candidate.getIsApproved() == null || !candidate.getIsApproved()) {
	        throw new BusinessValidationException("Cannot update eligibility: Candidate is not approved by faculty");
	    }
	    
	    // Update eligibility status
	    candidate.setIsEligible(isEligible);
	    candidateRepository.save(candidate);
	}


    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Candidate getCandidateById(String id) {
        return candidateRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Candidate not found with id: " + id));
    }


	public String getStudentIdByEmail(String studentEmail) {
		return studentRepository.findIdByStudentEmailId(studentEmail);
	}
    
}
