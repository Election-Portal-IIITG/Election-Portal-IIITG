package com.iiitg.election.services.EmailService;

import java.util.Map;

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
	
	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public EmailType getEmailType() {
		return emailType;
	}
	public void setEmailType(EmailType emailType) {
		this.emailType = emailType;
	}
	public Map<String, Object> getTemplateModel() {
		return templateModel;
	}
	public void setTemplateModel(Map<String, Object> templateModel) {
		this.templateModel = templateModel;
	}


	@Override
	public String toString() {
		return "EmailRequest [to=" + to + ", subject=" + subject + ", emailType=" + emailType + ", templateModel="
				+ templateModel + "]";
	}


	
	
	
}
