package org.sds.elevateconnect.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import org.sds.elevateconnect.model.auth.Permission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // Check method-level annotation first
        RequirePermission methodAnnotation = handlerMethod.getMethodAnnotation(RequirePermission.class);
        RequirePermission classAnnotation = handlerMethod.getBeanType().getAnnotation(RequirePermission.class);

        RequirePermission annotation = methodAnnotation != null ? methodAnnotation : classAnnotation;

        // If no RequirePermission annotation is present, the request is allowed by default
        if (annotation == null) {
            return true; // No authorization required
        }

        // Check permissions
        boolean hasAccess;
        Permission[] requiredPermissions = annotation.value();

        if (hasAdminPermission()) {
            hasAccess = true;
        } else if (annotation.requireAll()) {
            hasAccess = hasAllPermissions(requiredPermissions);
        } else {
            hasAccess = hasAnyPermission(requiredPermissions);
        }

        if (!hasAccess) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\":\"Access denied: Insufficient permissions\"}");
            response.setContentType("application/json");
            return false;
        }

        return true;
    }

    // The following are helper methods for comparing authenticated and required permissions
    private boolean hasAllPermissions(Permission[] requiredPermissions) {
        // Fetch authenticated permissions strings and change the permission array to a list of strings
        List<String> listOfAuthenticatedPermissionStrings = getAuthenticatedPermissionsList();
        List<String> listOfRequiredPermissionStrings = Arrays.stream(requiredPermissions).map(Permission::getStringValue).toList();

        // Check if all the required permissions are contained in the authenticated ones. Intellij suggested to wrap in a hashset to improve performance
        return new HashSet<>(listOfAuthenticatedPermissionStrings).containsAll(listOfRequiredPermissionStrings);
    }

    private boolean hasAnyPermission(Permission[] requiredPermissions) {
        // Fetch authenticated permissions strings and change the permission array to a list of strings
        List<String> listOfAuthenticatedPermissionStrings = getAuthenticatedPermissionsList();
        List<String> listOfRequiredPermissionStrings = Arrays.stream(requiredPermissions).map(Permission::getStringValue).toList();

        // Check if any of the required permissions are contained in the authenticated ones
        return listOfRequiredPermissionStrings.stream().anyMatch(listOfAuthenticatedPermissionStrings::contains);
    }

    // If the user has Permission.ALL_PERMISSIONS, they are granted access to all permissions. This is the "admin permission"
    private boolean hasAdminPermission() {
        List<String> listOfAuthenticatedPermissionStrings = getAuthenticatedPermissionsList();

        for (String permissionString : listOfAuthenticatedPermissionStrings) {
            if (Objects.equals(permissionString, Permission.ALL_PERMISSIONS.getValue())) {
                return true;
            }
        }

        return false;
    }

    // From spring security context, fetch a list of the permission strings the user has available
    private List<String> getAuthenticatedPermissionsList() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }
}