package com.example.validation;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.annotation.EmailValidation;
import com.example.domain.Administrator;
import com.example.repository.AdministratorRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailUniqueValidation implements ConstraintValidator<EmailValidation, String>{
	@Autowired
	AdministratorRepository repository;
	
	@Override
	public void initialize(EmailValidation constraintAnnotation) {
	}
	
	@Override
	public boolean isValid(String value,ConstraintValidatorContext context) {
		Administrator administrator = repository.findByMailAddress(value);
		
		if(administrator!=null) {
			return false;
		}
		return true;
	}
}
