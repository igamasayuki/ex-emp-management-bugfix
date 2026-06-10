package com.example.service;

import com.example.domain.Administrator;
import com.example.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 管理者情報を登録します.
     *
     * @param administrator 管理者情報
     */
    public boolean insert(Administrator administrator) {
        Administrator existMail =
                administratorRepository.findByMailAddress(administrator.getMailAddress());

        if (existMail != null){
            return false;
        }

        administratorRepository.insert(administrator);
        return true;
    }

    /**
     * ログインをします.
     *
     * @param mailAddress メールアドレス
     * @param password    パスワード
     * @return 管理者情報 存在しない場合はnullが返ります
     */
    public Administrator login(String mailAddress, String password) {
        Administrator administrator = administratorRepository.findByMailAddressAndPassword(mailAddress, password);
        return administrator;
    }
}
