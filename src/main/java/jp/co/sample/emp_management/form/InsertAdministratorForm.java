package jp.co.sample.emp_management.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 管理者情報登録時に使用するフォーム.
 * 
 * @author igamasayuki
 * 
 */
public class InsertAdministratorForm {
	/** 名前 */
	@NotBlank(message = "氏名は必ず入力してください。")
	private String name;

	/** メールアドレス */
	@NotBlank(message = "メールアドレスは必ず入力してください。")
	@Email(message = "メールアドレスの形式が不正です。")
	private String mailAddress;

	/** パスワード */
	@Pattern(regexp = "^([a-zA-Z0-9]{8,})$", message = "パスワードは8文字以上の英数字で入力してください。")
	private String password;

	/** 確認用パスワード */
	@Pattern(regexp = "^([a-zA-Z0-9]{8,})$", message = "確認用パスワードは8文字以上の英数字で入力してください。")
	private String comfirmPassword;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the mailAddress
	 */
	public String getMailAddress() {
		return mailAddress;
	}

	/**
	 * @param mailAddress the mailAddress to set
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public String getComfirmPassword() {
		return comfirmPassword;
	}

	public void setComfirmPassword(String comfirmPassword) {
		this.comfirmPassword = comfirmPassword;
	}

	@Override
	public String toString() {
		return "InsertAdministratorForm [name=" + name + ", mailAddress=" + mailAddress + ", password=" + password
				+ ", comfirmPassword=" + comfirmPassword + "]";
	}

}
