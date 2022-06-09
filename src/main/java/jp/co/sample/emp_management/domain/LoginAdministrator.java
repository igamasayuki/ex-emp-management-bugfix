package jp.co.sample.emp_management.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class LoginAdministrator extends User {
	
	private static final long serialVersionUID = 1L;
	private final Administrator administrator;
	
	public LoginAdministrator(Administrator administrator, Collection<GrantedAuthority> authorityList) {
		super(administrator.getMailAddress(), administrator.getPassword(), authorityList);
		this.administrator = administrator;
	}
	
	public Administrator getAdministrator() {
		return administrator;
	}

}
