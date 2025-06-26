package org.example.stage4.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CookieInfoController {

    @GetMapping("/debug-session")
    public ResponseEntity<?> debugSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        SecurityContext context = SecurityContextHolder.getContext();

        Map<String, Object> info = new HashMap<>();
        info.put("sessionId", session.getId());
        info.put("principal", context.getAuthentication().getName());
        info.put("authorities", context.getAuthentication().getAuthorities());

        return ResponseEntity.ok(info);
    }
}
