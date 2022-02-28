package jp.co.sample.emp_management.service;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.domain.LoginAdministrator;
import jp.co.sample.emp_management.repository.AdministratorRepository;

@Service
public class AdministratorDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private AdministratorRepository administratorRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Administrator administrator = administratorRepository.findByMailAddress(username);
		if (administrator == null) {
			throw new UsernameNotFoundException("そのメールアドレスは登録されていません");
		}
		Collection<GrantedAuthority> authorityList = new ArrayList<>();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println(administrator);
		if ("admin".equals(administrator.getName())) {
			authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
		return new LoginAdministrator(administrator, authorityList);
	}

}
