package com.interswitch.lms.security.util;

import com.interswitch.lms.entity.Auth;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static Auth getCurrentUser() {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (principal instanceof Auth) {
            return (Auth) principal;
        }

        return null;
    }

    public static String getCurrentUserEmail() {

        Auth user = getCurrentUser();
        return user != null ? user.getEmail() : null;
    }
}