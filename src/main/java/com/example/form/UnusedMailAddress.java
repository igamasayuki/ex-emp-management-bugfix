package com.example.form;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * メールアドレス重複ヴァリテーションのアノテーション.
 * @author matsumotoyuyya
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UnusedMailAddressValidator.class})

public @interface UnusedMailAddress {
	
	 String message() default "このメールアドレスは既に登録されています";
	    Class<?>[] groups() default {};
	    Class<? extends Payload>[] payload() default {};
	   

}
