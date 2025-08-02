package edu.yacoubi.tasks.htmxdemos.layoutProjekt.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LayoutDemoViewController {

    @GetMapping("/layoutdemo/home")
    public String showHomePage() {
        return "layoutdemo/home";
    }

    @GetMapping("/layoutdemo/contact")
    public String showContactPage() {
        return "layoutdemo/contact";
    }

    @GetMapping("/layoutdemo/privacy")
    public String showPrivacyPage() {
        return "layoutdemo/privacy";
    }
}
