//package com.proyecto.integrador.security;
//
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//
//import java.security.Key;
//import java.util.Base64;
//import java.util.Date;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class JwtUtil {
//
//    private final Key key;
//
//    public JwtUtil(@Value("${security.jwt.secret}") String secretKeyBase64) {
//        System.out.println("Clave recibida: " + secretKeyBase64); // Log para ver si se obtiene la clave
//
//        try {
//            if (secretKeyBase64 == null || secretKeyBase64.isEmpty()) {
//                System.err.println("ERROR: La clave secreta JWT no puede estar vacía");
//                throw new IllegalArgumentException("La clave secreta JWT no puede estar vacía");
//            }
//
//            byte[] decodedKey;
//            try {
//                decodedKey = Base64.getDecoder().decode(secretKeyBase64);
//                System.out.println("Clave decodificada correctamente, longitud: " + decodedKey.length + " bytes");
//            } catch (IllegalArgumentException e) {
//                System.err.println("ERROR al decodificar la clave Base64: " + e.getMessage());
//                throw e;
//            }
//
//            try {
//                this.key = Keys.hmacShaKeyFor(decodedKey);
//                System.out.println("Clave HMAC creada correctamente");
//            } catch (Exception e) {
//                System.err.println("ERROR al crear la clave HMAC: " + e.getMessage());
//                e.printStackTrace();
//                throw e;
//            }
//        } catch (Exception e) {
//            System.err.println("Excepción en el constructor de JwtUtil: " + e.getMessage());
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    private static final long EXPIRATION_TIME = 86400000; // 1 día
//
//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(key)
//                .compact();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (JwtException e) {
//            return false;
//        }
//    }
//
//    public String extractUsername(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//}
