package com.example.controller.step3_advance;

import com.example.controller.step3_advance.form.AdvanceInsertAdministratorForm;
import com.example.domain.Administrator;
import com.example.domain.SystemMessage;
import com.example.form.LoginForm;
import com.example.service.step3_advance.FullAdministratorService;
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
 * 管理者情報を操作するコントローラー（上級対応版）.
 * 完全に独立したフローを実現.
 */
@Controller("step3FullAdministratorController")
@RequestMapping("/advance")
public class FullAdministratorController {

    @Autowired
    private FullAdministratorService administratorService;

    @Autowired
    private HttpSession session;

    @ModelAttribute("advanceInsertAdministratorForm")
    public AdvanceInsertAdministratorForm setUpInsertAdministratorForm() {
        return new AdvanceInsertAdministratorForm();
    }

    @ModelAttribute
    public LoginForm setUpLoginForm() {
        return new LoginForm();
    }

    /**
     * ログイン画面を出力します.
     */
    @GetMapping("/")
    public String toLogin() {
        return "step3_advance/administrator/login";
    }

    @GetMapping("/toInsert")
    public String toInsert() {
        return "step3_advance/administrator/insert";
    }

    /**
     * 管理者情報を登録します（パスワードハッシュ化対応）.
     */
    @PostMapping("/insert")
    public String insert(@Validated @ModelAttribute("advanceInsertAdministratorForm") AdvanceInsertAdministratorForm form, BindingResult result, RedirectAttributes redirectAttributes) {
        
        if (administratorService.findByMailAddress(form.getMailAddress()) != null) {
            result.rejectValue("mailAddress", null, "そのメールアドレスはすでに登録されています");
        }

        if (form.getPassword() != null && !form.getPassword().equals(form.getPasswordConfirm())) {
            result.rejectValue("password", null, "パスワードが一致しません");
        }

        if (result.hasErrors()) {
            return "step3_advance/administrator/insert";
        }

        Administrator administrator = new Administrator();
        BeanUtils.copyProperties(form, administrator);
        
        // Service内部でハッシュ化が行われる
        administratorService.insert(administrator);
        
        redirectAttributes.addFlashAttribute("infoMessage", "管理者の登録が完了しました。ログインしてください。");

        return "redirect:/advance/"; // 上級専用ログイン画面へ戻る
    }

    /**
     * 別解：Enumを使ってメッセージを一括管理する登録処理.
     */
    @PostMapping("/insertWithEnum")
    public String insertWithEnum(@Validated @ModelAttribute("advanceInsertAdministratorForm") AdvanceInsertAdministratorForm form, BindingResult result, RedirectAttributes redirectAttributes) {
        if (administratorService.findByMailAddress(form.getMailAddress()) != null) {
            result.rejectValue("mailAddress", null, SystemMessage.MAIL_DUPLICATE.getText());
        }
        if (form.getPassword() != null && !form.getPassword().equals(form.getPasswordConfirm())) {
            result.rejectValue("password", null, SystemMessage.PASSWORD_MISMATCH.getText());
        }
        if (result.hasErrors()) {
            return "step3_advance/administrator/insert";
        }

        Administrator administrator = new Administrator();
        BeanUtils.copyProperties(form, administrator);
        administratorService.insert(administrator);
        
        // メッセージのキーもテキストも Enum から取得
        redirectAttributes.addFlashAttribute(SystemMessage.REGISTER_SUCCESS.getKey(), SystemMessage.REGISTER_SUCCESS.getText());

        return "redirect:/advance/";
    }

    /**
     * ログインします（ハッシュ照合対応）.
     */
    @PostMapping("/login")
    public String login(LoginForm form, RedirectAttributes redirectAttributes) {
        Administrator administrator = administratorService.login(form.getMailAddress(), form.getPassword());
        if (administrator == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "メールアドレスまたはパスワードが不正です。");
            return "redirect:/advance/";
        }

        // ログイン成功：セッションに名前を保存
        session.setAttribute("administratorName", administrator.getName());

        return "redirect:/advanceEmployee/showList";
    }
}
