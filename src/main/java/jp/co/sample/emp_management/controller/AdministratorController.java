package jp.co.sample.emp_management.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.form.InsertAdministratorForm;
import jp.co.sample.emp_management.form.LoginForm;
import jp.co.sample.emp_management.service.AdministratorService;

/**
 * 管理者情報を操作するコントローラー.
 * 
 * @author igamasayuki
 *
 */
@Controller
@RequestMapping("/")
public class AdministratorController {

	@Autowired
	private AdministratorService administratorService;

	@Autowired
	private HttpSession session;

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public InsertAdministratorForm setUpInsertAdministratorForm() {
		return new InsertAdministratorForm();
	}

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public LoginForm setUpLoginForm() {
		return new LoginForm();
	}

	/////////////////////////////////////////////////////
	// ユースケース：管理者を登録する
	/////////////////////////////////////////////////////
	/**
	 * 管理者登録画面を出力します.
	 * 
	 * @return 管理者登録画面
	 */
	@RequestMapping("/toInsert")
	public String toInsert(Model model, InsertAdministratorForm form) {
		return "administrator/insert";
	}

	/**
	 * 管理者情報を登録します.
	 * 
	 * @param form 管理者情報用フォーム
	 * @return ログイン画面へリダイレクト
	 */
	@RequestMapping("/insert")
	public String insert(@Validated InsertAdministratorForm form, BindingResult result,
			RedirectAttributes redirectAttributes, Model model, Map<String, String> errorMap) {

		Administrator admin = administratorService.searchMailAddress(form.getMailAddress());
		if (admin != null) {
			result.rejectValue("mailAddress", null, "そのメールアドレスは既に存在します");
		}

		if (result.hasErrors()) {
			return toInsert(model, form);
		}

		// メールアドレスを条件に１件取得してnullかどうか→無かったら登録

		BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
		String encodeedPassword = bcpe.encode(form.getPassword());
		form.setPassword(encodeedPassword);

		Administrator administrator = new Administrator();
		// フォームからドメインにプロパティ値をコピー
		BeanUtils.copyProperties(form, administrator);
		System.out.println("登録する管理者　パスワード　：" + administrator.getPassword());
		System.out.println("登録する管理者　：" + administrator);
		administratorService.insert(administrator);

		return "redirect:/";

	}

	/////////////////////////////////////////////////////
	// ユースケース：ログインをする
	/////////////////////////////////////////////////////
	/**
	 * ログイン画面を出力します.
	 * 
	 * @return ログイン画面
	 */
	@RequestMapping("/")
	public String toLogin() {
		return "administrator/login";
	}

	/**
	 * ログインします.
	 * 
	 * @param form 管理者情報用フォーム
	 * @return ログイン後の従業員一覧画面
	 */
	@RequestMapping("/login")
	public String login(LoginForm form, Model model) {
		BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
		String pass = bcpe.encode(form.getPassword());
		System.out.println("ログインする人のPASS ＝　" + bcpe.encode(form.getPassword()));
		System.out.println("ログインする人のPASS22222 ＝　" + pass);
		Administrator administrator = administratorService.searchMailAddress(form.getMailAddress());
		if (bcpe.matches(form.getPassword(), administrator.getPassword())) {

			return "forward:/employee/showList";
		}

		model.addAttribute("errorMessage", "メールアドレスまたはパスワードが不正です。");
		return "redirect:/toLogin()";

	}

	/////////////////////////////////////////////////////
	// ユースケース：ログアウトをする
	/////////////////////////////////////////////////////
	/**
	 * ログアウトをします. (SpringSecurityに任せるためコメントアウトしました)
	 * 
	 * @return ログイン画面
	 */
	@RequestMapping(value = "/logout")
	public String logout() {
		session.invalidate();
		return "redirect:/";
	}

}
