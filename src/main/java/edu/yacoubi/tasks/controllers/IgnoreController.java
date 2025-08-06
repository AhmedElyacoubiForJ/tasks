package edu.yacoubi.tasks.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

//@Controller
public class IgnoreController {

    @RequestMapping("/appspecific/com.chrome.devtools.json")
    public ResponseEntity<Void> ignoreRequest() {
        System.out.println("Ignoring request to /appspecific/com.chrome.devtools.json");
        return ResponseEntity.notFound().build();
    }
}
