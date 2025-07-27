package edu.yacoubi.tasks.htmxdemo.controllers;

import com.github.javafaker.Faker;
import edu.yacoubi.tasks.htmxdemo.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/htmxdemo")
@RequiredArgsConstructor
public class HtmxDemoController {

    private final Faker faker;

    @GetMapping("")
    public String index() {
        return "htmxdemo/index";
    }

    @GetMapping("/frag-info")
    public String getInfoFragment(Model model) {
        model.addAttribute("name", faker.name().fullName());
        model.addAttribute("email", faker.internet().emailAddress());
        model.addAttribute("city", faker.address().cityName());

        return "htmxdemo/fragments/frag-info";
    }

    @GetMapping("/list-items")
    public String getListItems(Model model) {
        Faker faker = new Faker();

        List<String> items = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            items.add(faker.company().name());
        }

        model.addAttribute("items", items);
        return "htmxdemo/fragments/list-items"; // Template kommt jetzt!
    }

    @GetMapping("/demo-form")
    public String showDemoForm(Model model) {
        model.addAttribute("person", new Person()); // Dummy-Objekt für das Formular
        return "htmxdemo/fragments/demo-form"; // Fragment kommt gleich!
    }

    @PostMapping("/submit-form")
    public String handleSubmit(@ModelAttribute Person person, Model model) {
        model.addAttribute("name", person.name());
        model.addAttribute("email", person.email());

        return "htmxdemo/fragments/form-success";
    }
}

