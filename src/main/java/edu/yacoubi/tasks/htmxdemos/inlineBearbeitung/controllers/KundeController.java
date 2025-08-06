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

    // 👉 1. Kundenliste anzeigen
    @GetMapping
    public String showTable(Model model) {
        List<Kunde> kunden = kundeService.findAll();
        model.addAttribute("kunden", kunden);
        return "htmxdemo/inlineEdit/pages/table";
    }

    // 👉 2. Einzelne Zeile als Formular laden (HTMX hx-get)
    @GetMapping("/edit/{id}")
    public String editRow(@PathVariable Long id, Model model) {

        Kunde kunde = kundeService.findById(id);
        System.out.println("Edit row for Kunde with ID: " + id);
        System.out.println("Found Kunde: " + kunde);

        model.addAttribute("kunde", kunde);
        return "htmxdemo/inlineEdit/fragments/rowEdit :: rowEdit";
    }


    @PostMapping("/update/{id}")
    public String updateRow(
            @PathVariable Long id,
            @ModelAttribute Kunde kunde,
            Model model) {
        System.out.println("Update row for Kunde with ID: " + id);
        System.out.println("Received Kunde: " + kunde);

        Kunde updated = kundeService.update(id, kunde);

        model.addAttribute("kunde", updated);

        return "htmxdemo/inlineEdit/fragments/tableRow :: row";
    }

    @GetMapping("/view/{id}")
    public String getViewFragment(@PathVariable Long id, Model model) {
        Kunde kunde = kundeService.findById(id);
        model.addAttribute("kunde", kunde);
        return "htmxdemo/inlineEdit/fragments/tableRow :: row";
    }
}

