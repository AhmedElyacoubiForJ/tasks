package edu.yacoubi.tasks.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/interceptor")
public class InterceptorToggleController {

    private final AtomicReference<String> activeInterceptor = new AtomicReference<>("console");

    @PostMapping("/switch")
    public ResponseEntity<?> switchInterceptor(@RequestParam String mode) {
        if (!List.of("console", "json").contains(mode)) {
            return ResponseEntity.badRequest().body("Invalid mode");
        }
        activeInterceptor.set(mode);
        return ResponseEntity.ok("Switched to " + mode);
    }

    @GetMapping
    public String getActiveInterceptor() {
        return activeInterceptor.get();
    }
}

