package com.example.controller.step4_final;

import com.example.domain.Administrator;
import com.example.form.InsertAdministratorForm;
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
 * 管理者情報を操作するコントローラー（完成版）.
 */
@Controller("step4FullAdministratorController")
@RequestMapping("/full")
public class FullAdministratorController {

    @Autowired
    private AdministratorService administratorService;

    @ModelAttribute
    public InsertAdministratorForm setUpInsertAdministratorForm() {
        return new InsertAdministratorForm();
    }

    @ModelAttribute
    public LoginForm setUpLoginForm() {
        return new LoginForm();
    }

    @GetMapping("/toInsert")
    public String toInsert() {
        return "administrator/insert";
    }

    /**
     * 管理者情報を登録します.
     */
    @PostMapping("/insert")
    public String insert(@Validated InsertAdministratorForm form, BindingResult result, RedirectAttributes redirectAttributes) {
        // メールアドレス重複チェック
        Administrator existAdmin = administratorService.findByMailAddress(form.getMailAddress());
        if (existAdmin != null) {
            result.rejectValue("mailAddress", null, "そのメールアドレスはすでに登録されています");
        }

        if (result.hasErrors()) {
            return "administrator/insert";
        }

        Administrator administrator = new Administrator();
        BeanUtils.copyProperties(form, administrator);
        administratorService.insert(administrator);
        
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
            return "redirect:/full/";
        }
        return "redirect:/employee/showList";
    }
}
