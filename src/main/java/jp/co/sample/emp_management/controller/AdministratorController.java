package jp.co.sample.emp_management.controller;

import java.util.Locale;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.form.InsertAdministratorForm;
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
	private MessageSource MessageSource;

	// @Autowired
	// private HttpSession session;

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
	// @ModelAttribute
	// public LoginForm setUpLoginForm() {
	// return new LoginForm();
	// }

	/////////////////////////////////////////////////////
	// ユースケース：管理者を登録する
	/////////////////////////////////////////////////////
	/**
	 * 管理者登録画面を出力します.
	 *
	 * @return 管理者登録画面
	 */
	@RequestMapping("/toInsert")
	public String toInsert() {
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
			Model model) {
		Administrator dupulicateEmailCheck =
				administratorService.findByMailAddress(form.getMailAddress());
		if (dupulicateEmailCheck != null) {
			result.rejectValue("mailAddress", "dupulicateEmailError");
		}
		if (!form.getPasswordConfirmation().equals(form.getPassword())) {
			result.rejectValue("passwordConfirmation", "passwordConfirmError");
		}
		if (result.hasErrors() || dupulicateEmailCheck != null
				|| !form.getPasswordConfirmation().equals(form.getPassword())) {
			return toInsert();
		}
		Administrator administrator = new Administrator();
		// フォームからドメインにプロパティ値をコピー
		BeanUtils.copyProperties(form, administrator);
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
	public String toLogin(Model model, String err) {
		if (err != null) {
			model.addAttribute("errorMessage",
					MessageSource.getMessage("errorMessage", new String[] {}, Locale.getDefault()));
		}
		return "administrator/login";
	}

	// /**
	// * ログインします.
	// *
	// * @param form 管理者情報用フォーム
	// * @param result エラー情報格納用オブッジェクト
	// * @return ログイン後の従業員一覧画面
	// */
	// @RequestMapping("/login")
	// public String login(LoginForm form, BindingResult result, Model model) {
	// System.out.println(form);
	// Administrator administrator =
	// administratorService.login(form.getMailAddress(), form.getPassword());
	// if (administrator == null) {
	// model.addAttribute("errorMessage", "メールアドレスまたはパスワードが不正です。");
	// return toLogin();
	// }
	// Administrator loginUser = administratorService
	// .findByMailAddressAndPassward(form.getMailAddress(), form.getPassword());
	// session.setAttribute("username", loginUser.getName());
	// return "forward:/employee/showList";
	// }

	// /////////////////////////////////////////////////////
	// // ユースケース：ログアウトをする
	// /////////////////////////////////////////////////////
	// /**
	// * ログアウトをします. (SpringSecurityに任せるためコメントアウトしました)
	// *
	// * @return ログイン画面
	// */
	// @RequestMapping(value = "/logout")
	// public String logout() {
	// session.invalidate();
	// return "redirect:/";
	// }

}
