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
    public void insert(Administrator administrator) {
        administratorRepository.insert(administrator);
    }

    /**
     * 指定されたメールアドレスが登録済みか確認します.
     *
     * @param mailAddress メールアドレス
     * @return 登録済みの場合true
     */
    public boolean existsByMailAddress(String mailAddress) {
        // DBの一意制約エラーに任せると500エラーになるため、登録前に業務エラーとして判定する。
        return administratorRepository.findByMailAddress(mailAddress) != null;
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
