package com.example.controller.step5_beyond;

import com.example.controller.step5_beyond.form.BeyondInsertAdministratorForm;
import com.example.domain.Administrator;
import com.example.form.LoginForm;
import com.example.service.step5_beyond.BeyondAdministratorService;
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
 * 追加課題(23-1, 27-1) 管理者コントローラー.
 */
@Controller("beyondAdministratorController")
@RequestMapping("/beyond")
public class BeyondAdministratorController {

    @Autowired
    private BeyondAdministratorService administratorService;

    @Autowired
    private HttpSession session;

    @ModelAttribute("beyondInsertAdministratorForm")
    public BeyondInsertAdministratorForm setUpInsertAdministratorForm() {
        return new BeyondInsertAdministratorForm();
    }

    @ModelAttribute
    public LoginForm setUpLoginForm() {
        return new LoginForm();
    }

    @GetMapping("/")
    public String toLogin() {
        return "step5_beyond/administrator/login";
    }

    @GetMapping("/toInsert")
    public String toInsert() {
        return "step5_beyond/administrator/insert";
    }

    @PostMapping("/insert")
    public String insert(
            @Validated @ModelAttribute("beyondInsertAdministratorForm") BeyondInsertAdministratorForm form,
            BindingResult result, RedirectAttributes redirectAttributes) {

        if (administratorService.findByMailAddress(form.getMailAddress()) != null) {
            result.rejectValue("mailAddress", null, "そのメールアドレスはすでに登録されています");
        }
        if (form.getPassword() != null && !form.getPassword().equals(form.getPasswordConfirm())) {
            result.rejectValue("password", null, "パスワードが一致しません");
        }
        if (result.hasErrors()) {
            return "step5_beyond/administrator/insert";
        }

        Administrator administrator = new Administrator();
        BeanUtils.copyProperties(form, administrator);
        administratorService.insert(administrator);

        redirectAttributes.addFlashAttribute("infoMessage", "administrator.register.success");
        return "redirect:/beyond/";
    }

    @PostMapping("/login")
    public String login(LoginForm form, RedirectAttributes redirectAttributes) {
        Administrator administrator = administratorService.login(form.getMailAddress(), form.getPassword());
        if (administrator == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "administrator.login.failed");
            return "redirect:/beyond/";
        }
        session.setAttribute("administratorName", administrator.getName());
        session.setAttribute("administratorMail", administrator.getMailAddress());
        return "redirect:/beyondEmployee/showList";
    }

    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("logoutMessage", "logout.success");
        return "redirect:/beyond/";
    }
}
