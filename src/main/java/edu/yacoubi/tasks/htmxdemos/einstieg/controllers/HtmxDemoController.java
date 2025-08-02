package edu.yacoubi.tasks.htmxdemos.einstieg.controllers;

import com.github.javafaker.Faker;
import edu.yacoubi.tasks.htmxdemos.einstieg.model.Person;
import edu.yacoubi.tasks.htmxdemos.fake.FakerProvider;
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

    private final FakerProvider fakerProvider;

    @GetMapping("")
    public String index() {
        return "htmxdemo/einstieg/index";
    }

    @GetMapping("/frag-info")
    public String getInfoFragment(Model model) {
        model.addAttribute("name", fakerProvider.getFaker().name().fullName());
        model.addAttribute("email", fakerProvider.getFaker().internet().emailAddress());
        model.addAttribute("city", fakerProvider.getFaker().address().cityName());

        return "htmxdemo/einstieg/fragments/frag-info";
    }

    @GetMapping("/list-items")
    public String getListItems(Model model) {
        Faker faker = new Faker();

        List<String> items = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            items.add(faker.company().name());
        }

        model.addAttribute("items", items);
        return "htmxdemo/einstieg/fragments/list-items"; // Template kommt jetzt!
    }

    @GetMapping("/demo-form")
    public String showDemoForm(Model model) {
        model.addAttribute("person", new Person()); // Dummy-Objekt für das Formular
        return "htmxdemo/einstieg/fragments/demo-form"; // Fragment kommt gleich!
    }

    @PostMapping("/submit-form")
    public String handleSubmit(@ModelAttribute Person person, Model model) {
        model.addAttribute("name", person.name());
        model.addAttribute("email", person.email());

        return "htmxdemo/einstieg/fragments/form-success";
    }
}

