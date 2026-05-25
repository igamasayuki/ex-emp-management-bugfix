package com.example.controller;

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
 * 管理者情報を操作するコントローラー（完成形）.
 */
@Controller
@RequestMapping("/")
public class AdministratorController {

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
     * 管理者登録（1-1, 1-2, 1-3, 1-4, 1-5）.
     */
    @PostMapping("/insert")
    public String insert(@Validated @ModelAttribute InsertAdministratorForm form, BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (administratorService.findByMailAddress(form.getMailAddress()) != null) {
            result.rejectValue("mailAddress", null, "そのメールアドレスはすでに登録されています");
        }

        if (form.getPassword() != null && !form.getPassword().equals(form.getPasswordConfirm())) {
            result.rejectValue("password", null, "パスワードが一致しません");
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
}
