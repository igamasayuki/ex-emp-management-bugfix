package com.example.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * ログイン済みか確認するFilter.
 */
@Component
public class LoginCheckFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if (path.startsWith("/employee/")) {
            HttpSession session = request.getSession(false);
            // ログイン成功時に保存する値がなければ、従業員画面への直URLアクセスとしてログイン画面へ戻す。
            if (session == null || session.getAttribute("administratorName") == null) {
                response.sendRedirect(request.getContextPath() + "/");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
