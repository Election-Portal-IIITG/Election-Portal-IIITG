package com.iiitg.election.services.EmailService;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EmailType {
	PASSWORD_NOTIFICATION, ELECTION_INFO, BOOTH_ROOM_DETAILS, OTP_NOTIFICATION;

}
