package com.example.service;

import com.example.domain.Administrator;
import com.example.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 管理者情報を登録します.
     *
     * @param administrator 管理者情報
     */
    public void insert(Administrator administrator) {
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

        //パスワードが一致しているか判定
        boolean isCorrectPassword = passwordEncoder.matches(password, administrator.getPassword());
        if(!isCorrectPassword){
            return null;
        }

        return administrator;
    }

    /**
     * 指定したメールアドレスが存在するか判定します.
     * 存在する場合はtrue、存在しない場合はfalseを返します。
     *
     * @param mailAddress 確認したいメールアドレス
     * @return 存在する場合はtrue、存在しない場合はfalse
     */
    public boolean existMailAddress(String mailAddress) {
        Administrator administrator = administratorRepository.findByMailAddress(mailAddress);

        return administrator != null;
    }

    /**
     * パスワードをハッシュ化して返します.
     *
     * @param password パスワード
     * @return ハッシュ化されたパスワード
     */
    public String passwordHashing(String password) {
        return passwordEncoder.encode(password);
    }
}
