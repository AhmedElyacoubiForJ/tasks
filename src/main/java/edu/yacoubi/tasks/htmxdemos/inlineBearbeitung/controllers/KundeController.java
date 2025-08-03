package edu.yacoubi.tasks.htmxdemos.inlineBearbeitung.controllers;

import edu.yacoubi.tasks.htmxdemos.inlineBearbeitung.model.Kunde;
import edu.yacoubi.tasks.htmxdemos.inlineBearbeitung.service.KundeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/inlineEdit")
@RequiredArgsConstructor
public class KundeController {

    private final KundeService kundeService;

    @GetMapping
    public String showTable(Model model) {
        List<Kunde> kunden = kundeService.findAll();
        model.addAttribute("kunden", kunden);
        kunden.stream().forEach(
                System.out::println
        );
        return "htmxdemo/inlineEdit/pages/table";
    }

    @GetMapping("/edit/{id}")
    public String editRow(@PathVariable Long id, Model model) {
        model.addAttribute("kunde", kundeService.findById(id));
        return "htmxdemo/inlineEdit/fragments/rowEdit :: rowEdit";
    }

    @PostMapping("/update/{id}")
    public String updateRow(
            @PathVariable Long id,
            @ModelAttribute Kunde kunde,
            Model model) {
        Kunde updated = kundeService.update(id, kunde);
        model.addAttribute("kunde", updated);
        return "htmxdemo/inlineEdit/fragments/tableRow :: rowView";
    }
}

