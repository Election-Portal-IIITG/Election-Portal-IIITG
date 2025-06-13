package com.iiitg.election.services.EmailService;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EmailType {
	PASSWORD_NOTIFICATION, ELECTION_INFO, BOOTH_ROOM_DETAILS, OTP_NOTIFICATION;

	@JsonCreator
	public static EmailType fromString(String value) {
		System.err.println("Trying to convert emailType: " + value);
		if (value == null) {
			return null;
		}
		try {
			return EmailType.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid email type: " + value + ". Valid values are: "
					+ java.util.Arrays.toString(EmailType.values()));
		}
	}

	@JsonValue
	public String toValue() {
		return this.name();
	}

}
