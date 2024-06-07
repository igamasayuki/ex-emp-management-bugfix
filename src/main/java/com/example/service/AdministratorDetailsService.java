package com.example.service;

import com.example.domain.Administrator;
import com.example.domain.LoginAdministrator;
import com.example.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * ログイン処理用のサービス.
 *
 * @author rui.inoue
 */
@Service
public class AdministratorDetailsService implements UserDetailsService {

    @Autowired
    private AdministratorRepository administratorRepository;

    @Override
    public UserDetails loadUserByUsername(String mailAddress) throws UsernameNotFoundException {
        Administrator administrator = administratorRepository.findByMailAddress(mailAddress);

        if(administrator == null){
            throw new UsernameNotFoundException("Not found mail address:" + mailAddress);
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new LoginAdministrator(administrator, authorities);
    }
}
