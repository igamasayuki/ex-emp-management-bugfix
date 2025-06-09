package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Administrator;
import com.example.repository.AdministratorRepository;

/**
 * 管理者情報を操作するサービス.
 * 
 * @author igamasayuki
 *
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

	/**
	 * 指定されたメールアドレスを持つ管理者が存在するかどうかを判定する。
	 *
	 * @param mailAddress 管理者のメールアドレス
	 * @return 該当する管理者が存在すれば {@code true}、存在しなければ {@code false}
	 */
	public boolean isAdminExists(String mailAddress) {
        return administratorRepository.findByMailAddress(mailAddress) != null;
    }
}
