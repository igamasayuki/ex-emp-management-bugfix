package com.example.service;

import com.example.domain.Administrator;
import com.example.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Spring Security で使用するユーザー詳細情報を取得するサービス.
 */
@Service
public class LoginAdministratorDetailsService implements UserDetailsService {

    @Autowired
    private AdministratorRepository administratorRepository;

    /**
     * メールアドレス（Spring Security上はusername）からユーザー情報を取得します.
     */
    @Override
    public UserDetails loadUserByUsername(String mailAddress) throws UsernameNotFoundException {
        Administrator administrator = administratorRepository.findByMailAddress(mailAddress);
        if (administrator == null) {
            throw new UsernameNotFoundException("そのメールアドレスは登録されていません: " + mailAddress);
        }

        // 権限のリストを作成（今回は一律 ROLE_ADMIN）
        Collection<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        // Spring Security の User オブジェクトを返却
        return new User(administrator.getMailAddress(), administrator.getPassword(), authorityList);
    }
}
