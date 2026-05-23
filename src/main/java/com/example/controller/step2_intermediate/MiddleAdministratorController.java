package com.example.controller.step2_intermediate;

import com.example.controller.step2_intermediate.form.MiddleInsertAdministratorForm;
import com.example.domain.Administrator;
import com.example.form.LoginForm;
import com.example.service.step2_intermediate.MiddleAdministratorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 管理者情報を操作するコントローラー（中級対応版）.
 */
@Controller
@RequestMapping("/middle")
public class MiddleAdministratorController {

    @Autowired
    private MiddleAdministratorService administratorService;

    @Autowired
    private HttpSession session;

    @ModelAttribute("middleInsertAdministratorForm")
    public MiddleInsertAdministratorForm setUpInsertAdministratorForm() {
        return new MiddleInsertAdministratorForm();
    }

    @ModelAttribute
    public LoginForm setUpLoginForm() {
        return new LoginForm();
    }

    @GetMapping("/toInsert")
    public String toInsert() {
        return "step2_intermediate/administrator/insert";
    }

    /**
     * 管理者情報を登録します.
     * (1-1) 画面遷移：登録後はログイン画面へリダイレクトし、フラッシュメッセージを表示
     * (1-2) 入力値エラー処理：バリデーション実装、エラー時は入力値を保持して登録画面へ
     * (1-3) Eメール重複チェック：登録済みのEメールの場合はエラーを画面に表示
     * (1-4) ダブルサブミット対策：登録画面(HTML)側のonsubmitでボタンを非活性化
     */
    @PostMapping("/insert")
    public String insert(@Validated @ModelAttribute("middleInsertAdministratorForm") MiddleInsertAdministratorForm form, BindingResult result, RedirectAttributes redirectAttributes) {
        
        // (1-3) Eメール重複チェック
        if (administratorService.findByMailAddress(form.getMailAddress()) != null) {
            result.rejectValue("mailAddress", null, "そのメールアドレスはすでに登録されています");
        }

        // (1-5) 確認用パスワードチェック
        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            result.rejectValue("password", null, "パスワードが一致しません");
        }

        if (result.hasErrors()) {
            return "step2_intermediate/administrator/insert";
        }

        Administrator administrator = new Administrator();
        BeanUtils.copyProperties(form, administrator);
        administratorService.insert(administrator);
        
        // (1-1) 登録完了のフラッシュメッセージを設定
        redirectAttributes.addFlashAttribute("infoMessage", "管理者の登録が完了しました。ログインしてください。");

        return "redirect:/";
    }

    @GetMapping("/")
    public String toLogin() {
        return "administrator/login";
    }

    @PostMapping("/login")
    public String login(LoginForm form, RedirectAttributes redirectAttributes) {
        Administrator administrator = administratorService.login(form.getMailAddress(), form.getPassword());
        if (administrator == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "メールアドレスまたはパスワードが不正です。");
            return "redirect:/middle/";
        }

        // (2-1) ログイン者名をセッションに格納
        session.setAttribute("administratorName", administrator.getName());

        return "redirect:/middleEmployee/showList";
    }
}
