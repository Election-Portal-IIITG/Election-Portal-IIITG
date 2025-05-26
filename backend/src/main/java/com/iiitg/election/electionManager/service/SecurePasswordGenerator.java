package com.iiitg.election.electionManager.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SecurePasswordGenerator {
	private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
	private static final String DIGITS = "0123456789";
	private static final String SPECIAL = "!@#$%^&*()-_=+[]{}|;:,.<>?/";

	private final SecureRandom random = new SecureRandom();

	public String generatePassword(int length) {
		if (length < 8) {
            throw new IllegalArgumentException("Password length must be at least 8 characters");
        }
		
		List<Character> passwordChars = new ArrayList<>();
		
		passwordChars.add(UPPER.charAt(random.nextInt(UPPER.length())));
        passwordChars.add(LOWER.charAt(random.nextInt(LOWER.length())));
        passwordChars.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        passwordChars.add(SPECIAL.charAt(random.nextInt(SPECIAL.length())));
        
        String allChars = UPPER + LOWER + DIGITS + SPECIAL;
        
        for (int i = 4; i < length; i++) {
            passwordChars.add(allChars.charAt(random.nextInt(allChars.length())));
        }
        
        Collections.shuffle(passwordChars, random);
        
        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }

        return password.toString();
	}

}
