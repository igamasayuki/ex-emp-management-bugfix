package jp.co.sample.emp_management.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * ログイン時に使用するフォーム.
 * 
 * @author igamasayuki
 * 
 */
public class LoginForm {
	
	/** メールアドレス */
	@NotNull(message="メールアドレスを入力してください。")
	@Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
	private String mailAddress;
	/** パスワード */
	@NotNull(message = "パスワードを入力してください。")
	@Size(min = 1, message = "値を入力してください")
	private String password;
	
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

	@Override
	public String toString() {
		return "LoginForm [mailAddress=" + mailAddress + ", password=" + password + "]";
	}

}
