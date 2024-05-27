package com.example.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * 管理者情報登録時に使用するフォーム.
 * 
 * @author igamasayuki
 * 
 */
public class InsertAdministratorForm {
	/** 名前 */
	@NotNull(message = "名前は必ず入力してください")
	private String name;
	/** メールアドレス */
	@NotNull(message = "メールアドレスは必ず入力してください")
	@Email(message = "正しいメールアドレスの書式で入力してください 例）abc@def.jp")
	private String mailAddress;
	/** パスワード */
	@NotNull(message = "パスワードは必ず入力してください")
	private String password;

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

	@Override
	public String toString() {
		return "InsertAdministratorForm [name=" + name + ", mailAddress=" + mailAddress + ", password=" + password
				+ "]";
	}

}
