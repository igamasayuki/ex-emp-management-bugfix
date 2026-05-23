package com.example.service.step3_advance;

import com.example.domain.Administrator;
import com.example.repository.step3_advance.FullAdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 管理者情報を操作するサービス（上級対応版）.
 */
@Service
@Transactional
public class FullAdministratorService {
	
    @Autowired
    private FullAdministratorRepository administratorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 管理者情報を登録します（パスワード暗号化対応）.
     * (5-3) 上級対応
     */
    public void insert(Administrator administrator) {
        // パスワードをハッシュ化してセット
        String encodedPassword = passwordEncoder.encode(administrator.getPassword());
        administrator.setPassword(encodedPassword);
        administratorRepository.insert(administrator);
    }

    public Administrator findByMailAddress(String mailAddress) {
        return administratorRepository.findByMailAddress(mailAddress);
    }

    /**
     * ログインをします（ハッシュ化パスワード照合対応）.
     * (5-3) 上級対応
     */
    public Administrator login(String mailAddress, String password) {
        Administrator administrator = administratorRepository.findByMailAddress(mailAddress);
        if (administrator == null) {
            return null;
        }

        // 入力された平文パスワードと、DBのハッシュ化パスワードを照合
        if (passwordEncoder.matches(password, administrator.getPassword())) {
            return administrator;
        }
        
        return null;
    }
}
