package com.example.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.validation.EmailUniqueValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

//アノテーションのドキュメント化を有効にする
@Documented
//アノテーションが検証されるクラスを指定する。validatedBy属性が空
@Constraint(validatedBy = {EmailUniqueValidation.class})
//アノテーションが適用できる要素を指定する。field及びANNOTATION_TYPEに適応可能
@Target({ElementType.FIELD,ElementType.ANNOTATION_TYPE})
//アノテーションが実行時に有効であることを指定する
@Retention(RetentionPolicy.RUNTIME)
//アノテーションが検証に失敗した場合に単一の違反として報告される
@ReportAsSingleViolation
public @interface EmailValidation {
	//アノテーションが失敗した時に表示されるエラーメッセージを指定
	String message() default "このメールアドレスは既に登録されています";	
	//グループメソッドは、バリデーションのグループを指定する	
	Class<?>[] groups() default {};
	//ペイロードメソッドは、カスタムのペイロードオブジェクトを指定するために使用
    Class<? extends Payload>[] payload() default {};
	
	//アノテーションが適用できる要素を指定
	@Target({ElementType.FIELD,ElementType.ANNOTATION_TYPE})
	//アノテーションが実行時に有効であることを指定
	@Retention(RetentionPolicy.RUNTIME)
	//アノテーションがドキュメント化を有効であることを指定
	@Documented	
	//アノテーションのリストの値を指定
	public @interface list{
		EmailValidation value();
	}
}
