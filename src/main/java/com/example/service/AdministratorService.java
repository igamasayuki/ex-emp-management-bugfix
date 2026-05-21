package com.example.service;

import com.example.domain.Administrator;
import com.example.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 管理者情報を操作するサービス.
 *
 * @author igamasayuki
 */
@Service
@Transactional
public class AdministratorService {
	
    @Autowired
    private AdministratorRepository administratorRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 管理者情報を登録します.
     *
     * @param administrator 管理者情報
     */
    public void insert(Administrator administrator) {
        // パスワードは復元できないハッシュ値としてDBに保存する。bcryptはソルトも内部で扱う。
        administrator.setPassword(passwordEncoder.encode(administrator.getPassword()));
        administratorRepository.insert(administrator);
    }

    /**
     * ログインをします.
     *
     * @param mailAddress メールアドレス
     * @param password    パスワード
     * @return 管理者情報 存在しない場合はnullが返ります
     */
    public Administrator login(String mailAddress, String password) {
        Administrator administrator = administratorRepository.findByMailAddress(mailAddress);
        if (administrator == null) {
            return null;
        }
        // 入力値をハッシュ化して比較するのではなく、bcryptのmatchesで保存済みハッシュと照合する。
        if (!passwordEncoder.matches(password, administrator.getPassword())) {
            return null;
        }
        return administrator;
    }
}
