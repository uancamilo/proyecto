package com.proyecto.integrador.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

public class JwtCookieUtil {

    public static void attachJwtCookie(HttpServletResponse response, String token, boolean isProduction) {
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(isProduction)
                .sameSite("None")
                .path("/")
                .maxAge(60 * 60)
                .build();

        response.setHeader("Set-Cookie", jwtCookie.toString());
    }
}
