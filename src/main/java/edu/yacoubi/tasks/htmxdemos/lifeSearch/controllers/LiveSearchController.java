package edu.yacoubi.tasks.htmxdemos.lifeSearch.controllers;

import edu.yacoubi.tasks.htmxdemos.lifeSearch.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/lifeSearch")
public class LiveSearchController {

    private final List<Person> database = List.of(
            new Person("Anna Schmidt", "anna@example.com"),
            new Person("Ahmed Yacoubi", "ahmed@example.com"),
            new Person("Angela Merkel", "angela@example.com"),
            new Person("Anton Maier", "anton@example.com")
    );

    @GetMapping("/search")
    public String searchPage() {
        return "htmxdemo/lifeSearch/pages/search";
    }

    @GetMapping("/search/results")
    public String getResults(@RequestParam String query, Model model) {
        var matches = database.stream()
                .filter(p -> p.name().toLowerCase().contains(query.toLowerCase()))
                .toList();

        model.addAttribute("results", matches);
        return "htmxdemo/lifeSearch/fragments/searchResults :: results";
    }
}
