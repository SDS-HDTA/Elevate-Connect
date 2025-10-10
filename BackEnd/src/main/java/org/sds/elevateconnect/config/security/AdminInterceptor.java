package org.sds.elevateconnect.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {
    private final String adminPassword;

    public AdminInterceptor(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String adminAuthenticationHeader = request.getHeader("ADMIN-AUTHENTICATION");

        if (adminAuthenticationHeader == null || !adminAuthenticationHeader.equals(adminPassword)) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
