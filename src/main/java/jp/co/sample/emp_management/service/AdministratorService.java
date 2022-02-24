package jp.co.sample.emp_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.repository.AdministratorRepository;

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

	// public Administrator load(Integer id) {
	// return administratorRepository.load(id);
	// }

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
	 * @param password パスワード
	 * @return 管理者情報 存在しない場合はnullが返ります
	 */
	public Administrator login(String mailAddress, String password) {
		Administrator administrator =
				administratorRepository.findByMailAddressAndPassward(mailAddress, password);
		return administrator;
	}

	/**
	 * メールアドレス重複チェックで使います
	 *
	 * @param email メールアドレス
	 * @return 管理者情報(nullでなければメール重複)
	 */
	public Administrator findByMailAddress(String email) {
		return administratorRepository.findByMailAddress(email);
	}

	/**
	 * ログインユーザ検索メソッド
	 *
	 * @param mailAddress フォーム入力メールアドレス
	 * @param password フォーム入力パスワード
	 * @return ログインユーザ 情報
	 */
	public Administrator findByMailAddressAndPassward(String mailAddress, String password) {
		return administratorRepository.findByMailAddressAndPassward(mailAddress, password);
	}
}
