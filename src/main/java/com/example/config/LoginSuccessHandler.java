package com.example.config;

import com.example.domain.Administrator;
import com.example.repository.AdministratorRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * ログイン成功時にセッションへ管理者名を格納します（2-1）.
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private AdministratorRepository administratorRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        String mailAddress = authentication.getName();
        Administrator administrator = administratorRepository.findByMailAddress(mailAddress);
        if (administrator != null) {
            HttpSession session = request.getSession();
            session.setAttribute("administratorName", administrator.getName());
        }
        response.sendRedirect(request.getContextPath() + "/employee/showList");
    }
}
