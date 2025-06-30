package com.iiitg.election.services.EmailService;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailRequest {
	private String to;
	private String subject;
	private EmailType emailType;
	private Map<String, Object> templateModel;
	
	
	
	
	public EmailRequest() {
		super();
	}


	public EmailRequest(String to, String subject, EmailType emailType,
			Map<String, Object> templateModel) {
		super();
		this.to = to;
		this.subject = subject;
		this.emailType = emailType;
		this.templateModel = templateModel;
	}
	
}
