package com.iiitg.election.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IIITGEmailValidator implements ConstraintValidator<ValidEmail, String> {

	private static final String IIITG_DOMAIN = "@iiitg.ac.in";
	
	@Override
	public void initialize(ValidEmail constraintAnnotation) {
		
	}
	
	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		if (email == null) {
            return false;
        }
        return email.endsWith(IIITG_DOMAIN);
	}
}
