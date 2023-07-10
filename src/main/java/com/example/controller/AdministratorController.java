package com.example.controller;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.Administrator;
import com.example.form.InsertAdministratorForm;
import com.example.form.LoginForm;
import com.example.service.AdministratorService;

import jakarta.servlet.http.HttpSession;

/**
 * 管理者情報を操作するコントローラー.
 * 
 * @author nagahashirisa
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
	@GetMapping("/toInsert")
	public String toInsert(Model model, InsertAdministratorForm form) {
		//tokenの生成
		String token = UUID.randomUUID().toString();
		//生成したトークンをセッションに格納する
		session.setAttribute("token", token);
		return "administrator/insert";
	}

	/**
	 * 管理者情報を登録します.
	 * 
	 * @param form 管理者情報用フォーム
	 * @return ログイン画面へリダイレクト
	 */
	@PostMapping("/")
	public String insert(
			@Validated InsertAdministratorForm form,BindingResult result,Model model
			) {
		if(result.hasErrors()) {
			return toInsert(model, form);
		}
		//確認用パスワード
		//formから得たパスワードの確認用パスワードが同じではない場合。error文が表示される
		if(!(form.getConfirmedPassword().equals(form.getPassword()))) {
			//errorがキー、””内がキーに基づく文章
			model.addAttribute("error", "確認用パスワードが不一致です。");
			//登録画面にerror文を入れて返す
			return toInsert(model,form);
		}
		//tokenを取得する(ここで、サーバーはセッションに保存されているトークンと一致するかを確認。)
		String token = (String) session.getAttribute("token");
		//tokenが存在しない場合はダブルサブミットエラーを出す。
		//(tokenが一致する場合は、サーバーはフォームを処理。トークンが一致しない場合はサーバーがダブルサブミットエラーを出す。)
		if(token == null) {
			return "administrator/insert";
		}
		//tokenを削除する
		session.removeAttribute("token");
		
		//管理者情報を登録する
		Administrator administrator = new Administrator();
		// フォームからドメインにプロパティ値をコピー
		BeanUtils.copyProperties(form, administrator);
		administratorService.insert(administrator);
		//ログイン画面にリダイレクトする
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
	@GetMapping("/")
	public String toLogin() {
		return "administrator/login";
	}

	/**
	 * ログインします.
	 * 
	 * @param form 管理者情報用フォーム
	 * @return ログイン後の従業員一覧画面
	 */
	@PostMapping("/login")
	public String login(LoginForm form, RedirectAttributes redirectAttributes) {
		Administrator administrator = administratorService.login(form.getMailAddress(), form.getPassword());
		if (administrator == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "メールアドレスまたはパスワードが不正です。");
			return "redirect:/";
		}
		//管理者情報を格納する（従業員一覧画面と詳細画面へ）
		session.setAttribute("administrator", administrator);
		return "redirect:/employee/showList";
	}

	/////////////////////////////////////////////////////
	// ユースケース：ログアウトをする
	/////////////////////////////////////////////////////
	/**
	 * ログアウトをします. (SpringSecurityに任せるためコメントアウトしました)
	 * 
	 * @return ログイン画面
	 */
	@GetMapping(value = "/logout")
	public String logout() {
		session.invalidate();
		return "redirect:/";
	}

}
