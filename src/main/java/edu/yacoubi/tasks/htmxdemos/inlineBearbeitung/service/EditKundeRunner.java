package edu.yacoubi.tasks.htmxdemos.inlineBearbeitung.service;

import edu.yacoubi.tasks.htmxdemos.inlineBearbeitung.model.Kunde;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class EditKundeRunner implements CommandLineRunner {

    private final KundeService kundeService;

    public EditKundeRunner(KundeService kundeService) {
        this.kundeService = kundeService;
    }

    @Override
    public void run(String... args) throws Exception {
        Long id = 2L;
        Kunde neuerKunde = new Kunde(id, "Sofia Meier", "sofia.meier@beispiel.de");

        System.out.printf("🔄 Aktualisiere Kunde mit ID %d%n", id);
        kundeService.update(id, neuerKunde);

        Kunde aktualisiert = kundeService.findById(id);
        System.out.printf("✅ Ergebnis: %d | %s | %s%n",
                aktualisiert.getId(), aktualisiert.getName(), aktualisiert.getEmail());
    }
}


