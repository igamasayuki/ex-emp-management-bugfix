package com.example.domain;

/**
 * 管理者情報を表すドメイン.
 * 
 * @author igamasayuki
 * 
 */
public class Administrator {
	/** id(主キー) */
	private Integer id;
	/** 名前 */
	private String name;
	/** メールアドレス */
	private String mailAddress;
	/** パスワード */
	private String password;
	/** 確認用パスワード */
	private String confirmpassword;

	/**
	 * 引数無しのコンストラクタ.
	 */
	public Administrator() {
	}

	/**
	 * 初期化用コンストラクタ.
	 * 
	 * @param id          id(主キー)
	 * @param name        名前
	 * @param mailAddress メールアドレス
	 * @param password    パスワード
	 * @param confirmpassword 確認用パスワード
	 */
	public Administrator(Integer id, String name, String mailAddress, String password, String confirmpassword) {
		this.id = id;
		this.name = name;
		this.mailAddress = mailAddress;
		this.password = password;
		this.confirmpassword = confirmpassword;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getConfirmpassword() {
		return confirmpassword;
	}

	public void setConfirmpassword(String confirmpassword) {
		this.confirmpassword = confirmpassword;
	}

	@Override
	public String toString() {
		return "Administrator{" +
				"id=" + id +
				", name='" + name + '\'' +
				", mailAddress='" + mailAddress + '\'' +
				", password='" + password + '\'' +
				", confirmpassword='" + confirmpassword + '\'' +
				'}';
	}
}
