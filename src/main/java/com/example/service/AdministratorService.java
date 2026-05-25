package com.example.service;

import com.example.domain.Administrator;
import com.example.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 管理者情報を操作するサービス.
 */
@Service
@Transactional
public class AdministratorService {

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 管理者情報を登録します（5-3: パスワードはBCryptでハッシュ化）.
     */
    public void insert(Administrator administrator) {
        String encodedPassword = passwordEncoder.encode(administrator.getPassword());
        administrator.setPassword(encodedPassword);
        administratorRepository.insert(administrator);
    }

    public Administrator findByMailAddress(String mailAddress) {
        return administratorRepository.findByMailAddress(mailAddress);
    }

    /**
     * ログイン照合（演習用パス・レガシー呼び出し向け）.
     * 本番ログインは Spring Security が担当します.
     */
    public Administrator login(String mailAddress, String password) {
        Administrator administrator = administratorRepository.findByMailAddress(mailAddress);
        if (administrator == null) {
            return null;
        }
        if (passwordEncoder.matches(password, administrator.getPassword())) {
            return administrator;
        }
        return null;
    }
}
