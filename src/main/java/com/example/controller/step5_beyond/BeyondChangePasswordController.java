package com.example.controller.step5_beyond;

import com.example.controller.step5_beyond.form.BeyondChangePasswordForm;
import com.example.service.step5_beyond.BeyondAdministratorService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * 追加課題(22-1) パスワード変更コントローラー.
 */
@Controller("beyondChangePasswordController")
@RequestMapping("/beyond")
public class BeyondChangePasswordController {

    private static final Logger log = LoggerFactory.getLogger(BeyondChangePasswordController.class);

    @Autowired
    private BeyondAdministratorService administratorService;

    @Autowired
    private HttpSession session;

    @ModelAttribute("beyondChangePasswordForm")
    public BeyondChangePasswordForm setUpForm() {
        return new BeyondChangePasswordForm();
    }

    @GetMapping("/changePassword")
    public String toChangePassword() {
        if (session.getAttribute("administratorMail") == null) {
            return "redirect:/beyond/";
        }
        return "step5_beyond/changePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(
            @Validated @ModelAttribute("beyondChangePasswordForm") BeyondChangePasswordForm form,
            BindingResult result, RedirectAttributes redirectAttributes) {

        String mail = (String) session.getAttribute("administratorMail");
        if (mail == null) {
            return "redirect:/beyond/";
        }
        if (!form.getNewPassword().equals(form.getNewPasswordConfirm())) {
            result.rejectValue("newPasswordConfirm", null, "パスワードが一致しません");
        }
        if (result.hasErrors()) {
            return "step5_beyond/changePassword";
        }

        try {
            administratorService.changePassword(mail, form.getCurrentPassword(), form.getNewPassword());
            session.invalidate();
            redirectAttributes.addFlashAttribute("infoMessage", "password.change.success");
            return "redirect:/beyond/";
        } catch (IllegalArgumentException e) {
            result.rejectValue("currentPassword", null, e.getMessage());
            return "step5_beyond/changePassword";
        } catch (Exception e) {
            log.error("パスワード変更に失敗しました mail={}", mail, e);
            result.reject(null, "パスワード変更に失敗しました");
            return "step5_beyond/changePassword";
        }
    }
}
