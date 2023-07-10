package com.example.form;

import com.example.annotation.EmailValidation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 管理者情報登録時に使用するフォーム.
 * 
 * @author nagahashirisa
 * 
 */
public class InsertAdministratorForm {
	/** 名前 */
	@NotBlank(message="名前を入力してください。")
	private String name;
	/** メールアドレス */
	@NotBlank(message="メールアドレスを入力してください。")
	@Email(message="入力したメールアドレスの形式が不正です。")
	/**既にDBに存在している場合はエラーが出る独自のバリデーション*/
	@EmailValidation
	private String mailAddress;
	/** パスワード */
	@NotBlank(message="パスワードを入力してください。")
	@Pattern(regexp="^[a-zA-Z0-9]{8,16}$", message="半角英数字で8文字以上16文字以内で入力してください。")
	private String password;
	/**確認用パスワード*/
	@NotBlank(message="確認用パスワードを入力してください。")
	public String confirmedPassword;

	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
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

	@Override
	public String toString() {
		return "InsertAdministratorForm [name=" + name + ", mailAddress=" + mailAddress + ", password=" + password
				+ ", confirmedPassword=" + confirmedPassword + "]";
	}

}
