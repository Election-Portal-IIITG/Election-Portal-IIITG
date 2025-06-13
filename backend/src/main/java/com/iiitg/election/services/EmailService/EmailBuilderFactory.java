package com.iiitg.election.services.EmailService;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class EmailBuilderFactory {
	
	private final Map<EmailType, String> templateMap;
	
	public EmailBuilderFactory() {
		templateMap = Map.of(
			EmailType.PASSWORD_NOTIFICATION, "password-email",
	        EmailType.ELECTION_INFO, "election-info-email",
	        EmailType.BOOTH_ROOM_DETAILS, "booth-room-email"	
		);
				
	}
	
	public String getTemplateName(EmailType type) {
		return templateMap.get(type);
	}
}
