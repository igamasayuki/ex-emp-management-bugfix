package com.example.form;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;

/**
 * 管理者情報登録時に使用するフォーム.
 * 
 * @author igamasayuki
 * 
 */
public class InsertAdministratorForm {
	/** 名前 */
	@NotBlank
	private String name;
	/** メールアドレス */
	@NotBlank(message = "メールアドレスを入力してください")
	@UnusedMailAddress
	private String mailAddress;
	/** パスワード */
	@NotBlank
	private String password;
	
	 /**
	 * 確認用パスワード*/
	private String passwordConfirmation;

	    @AssertTrue(message = "パスワードと確認用パスワードは同一にしてください。")
	    public boolean isPasswordValid() {
	        if (password == null || password.isEmpty()) {
	            return true;
	        }

	        return password.equals(passwordConfirmation);
	    } 

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	@Override
	public String toString() {
		return "InsertAdministratorForm [name=" + name + ", mailAddress=" + mailAddress + ", password=" + password
				+ ", passwordConfirmation=" + passwordConfirmation + "]";
	}
	
	

	
}
