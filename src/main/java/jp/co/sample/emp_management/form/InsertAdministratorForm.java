package jp.co.sample.emp_management.form;

import javax.validation.constraints.Email;

import org.hibernate.validator.constraints.Length;

/**
 * 管理者情報登録時に使用するフォーム.
 * 
 * @author igamasayuki
 * 
 */
public class InsertAdministratorForm {
	/** 名前 */
	@Length(min = 1, max = 20, message = "1文字以上20文字以内力して下さい。")
	private String name;
	/** メールアドレス */
	@Email
	@Length(min = 1, max = 30, message = "1文字以上30文字以内力して下さい。")
	private String mailAddress;
	/** パスワード */
	@Length(min = 8, max = 16, message = "8文字以上16文字以内力して下さい。")
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
