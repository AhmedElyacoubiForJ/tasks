package edu.yacoubi.tasks.htmxdemos.inlineBearbeitung.service;

import edu.yacoubi.tasks.htmxdemos.inlineBearbeitung.model.Kunde;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class KundeTestRunner implements CommandLineRunner {

    private final KundeService kundeService;

    public KundeTestRunner(KundeService kundeService) {
        this.kundeService = kundeService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🧾 Kundenliste:");

        for (Kunde k : kundeService.findAll()) {
            System.out.printf("➡️  %d | %s | %s%n", k.getId(), k.getName(), k.getEmail());
        }
    }
}

