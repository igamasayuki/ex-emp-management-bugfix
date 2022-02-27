package jp.co.sample.emp_management.domain;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class LoginAdministrator extends User {

	private final Administrator administrator;

	public LoginAdministrator(Administrator administrator,
			Collection<? extends GrantedAuthority> authorities) {
		super(administrator.getMailAddress(), administrator.getPassword(), authorities);
		this.administrator = administrator;
	}

	/**
	 * 管理者情報を返します.
	 *
	 * @return 管理者情報
	 */
	public Administrator getAdministrator() {
		return administrator;
	}
}
