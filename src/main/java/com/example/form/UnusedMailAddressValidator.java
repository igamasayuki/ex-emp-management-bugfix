package com.example.form;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.domain.Administrator;
import com.example.repository.AdministratorRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * メールアドレス重複ヴァリテーションのアノテーション実装クラス.
 * @author matsumotoyuyya
 *
 */
public class UnusedMailAddressValidator  implements ConstraintValidator<UnusedMailAddress, String> {
	
	
	@Autowired
	private AdministratorRepository repository;
	

    /**
     *メールアドレス重複検証.
     *@param value メールアドレス
     *@param context 
     *@return false 重複
     *@return true 重複なし
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        
    	Administrator admin = repository.findByMailAddress(value);
        if(admin!=null) {
        	System.out.println("１１１１１１");
            return false;
        }
        return true;
    }
}
