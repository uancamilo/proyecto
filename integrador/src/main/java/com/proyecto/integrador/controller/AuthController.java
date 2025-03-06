//package com.proyecto.integrador.controller;
//
//import com.proyecto.integrador.security.JwtUtil;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    private final JwtUtil jwtUtil;
//
//    public AuthController(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
//        String username = request.get("username");
//
//        if (username == null || username.isEmpty()) {
//            return ResponseEntity.badRequest().body(Map.of("error", "El nombre de usuario es obligatorio"));
//        }
//
//        String token = jwtUtil.generateToken(username);
//        return ResponseEntity.ok(Map.of("token", token));
//    }
//}