package com.example.controller.step1_beginner;

import com.example.controller.step1_beginner.form.JuniorInsertAdministratorForm;
import com.example.domain.Administrator;
import com.example.form.LoginForm;
import com.example.service.AdministratorService;
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
 * 管理者情報を操作するコントローラー（初級対応版）.
 */
@Controller
@RequestMapping("/junior")
public class JuniorAdministratorController {

    @Autowired
    private AdministratorService administratorService;

    @ModelAttribute("insertAdministratorForm")
    public JuniorInsertAdministratorForm setUpInsertAdministratorForm() {
        return new JuniorInsertAdministratorForm();
    }

    @ModelAttribute
    public LoginForm setUpLoginForm() {
        return new LoginForm();
    }

    @GetMapping("/toInsert")
    public String toInsert() {
        return "step1_beginner/administrator/insert";
    }

    /**
     * 管理者情報を登録します.
     * (1-1) 画面遷移：登録後はログイン画面へリダイレクトし、フラッシュメッセージを表示
     * (1-2) 入力値エラー処理：バリデーション実装、エラー時は入力値を保持して登録画面へ
     * (1-4) ダブルサブミット対策：登録画面(HTML)側のonsubmitでボタンを非活性化
     */
    @PostMapping("/insert")
    public String insert(@Validated @ModelAttribute("insertAdministratorForm") JuniorInsertAdministratorForm form, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // エラー時はフォームの値を保持したまま登録画面へフォワード
            return "step1_beginner/administrator/insert";
        }

        Administrator administrator = new Administrator();
        BeanUtils.copyProperties(form, administrator);
        administratorService.insert(administrator);
        
        // (1-1) 登録完了のフラッシュメッセージを設定
        redirectAttributes.addFlashAttribute("infoMessage", "管理者の登録が完了しました。ログインしてください。");

        // 登録後、初級専用ログイン画面へリダイレクト（フラッシュメッセージ表示用）
        return "redirect:/junior/";
    }

    @GetMapping("/")
    public String toLogin() {
        return "step1_beginner/administrator/login";
    }

    @PostMapping("/login")
    public String login(LoginForm form, RedirectAttributes redirectAttributes) {
        Administrator administrator = administratorService.login(form.getMailAddress(), form.getPassword());
        if (administrator == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "メールアドレスまたはパスワードが不正です。");
            return "redirect:/junior/";
        }
        return "redirect:/junior/employee/showList";
    }
}
