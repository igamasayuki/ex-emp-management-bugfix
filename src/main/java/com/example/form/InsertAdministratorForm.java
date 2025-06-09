package com.example.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 管理者情報登録時に使用するフォーム.
 * 
 * @author igamasayuki
 * 
 */
public class InsertAdministratorForm {

	/** 名前 */
	@NotBlank(message="値を入力してください")
	@Size(min=1, max=20, message="名前は1文字以上20文字以下で入力してください")
	private String name;

	/** メールアドレス */
	@NotBlank(message="値を入力してください")
	@Size(min=1, max=20, message="メールアドレスは1文字以上20文字以下で入力してください")
	private String mailAddress;

	/** パスワード */
	@NotEmpty(message="値を入れてください")
	private String password;

	/**確認用パスワード */
	@NotBlank(message="パスワードと同じ値を入力してください")
	private String confirmpassword;

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

	public String getConfirmpassword() {
		return confirmpassword;
	}

	public void setConfirmpassword(String confirmpassword) {
		this.confirmpassword = confirmpassword;
	}

	@Override
	public String toString() {
		return "InsertAdministratorForm{" +
				"name='" + name + '\'' +
				", mailAddress='" + mailAddress + '\'' +
				", password='" + password + '\'' +
				", confirmpassword='" + confirmpassword + '\'' +
				'}';
	}
}
