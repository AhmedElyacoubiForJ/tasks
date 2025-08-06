package edu.yacoubi.tasks.htmxdemos.inlineBearbeitung.service;

import edu.yacoubi.tasks.htmxdemos.inlineBearbeitung.model.Kunde;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
public class ListAndEditRunner implements CommandLineRunner {

    private final KundeService kundeService;

    @Override
    public void run(String... args) {
        System.out.println("📋 Aktuelle Kunden:");
        kundeService.findAll().forEach(k ->
                System.out.printf("➡️  %d | %s | %s%n", k.getId(), k.getName(), k.getEmail())
        );

        System.out.println("\n✏️ Ändere Kunde mit ID 3...");
        Kunde neuerKunde = new Kunde(3L, "Tim Neumann", "tim.neumann@beispiel.de");
        kundeService.update(3L, neuerKunde);

        Kunde aktualisiert = kundeService.findById(3L);
        System.out.printf("✅ Aktualisiert: %d | %s | %s%n",
                aktualisiert.getId(), aktualisiert.getName(), aktualisiert.getEmail());

        /*kundeService.findAll().forEach(k ->
                System.out.printf("➡️  %d | %s | %s%n", k.getId(), k.getName(), k.getEmail())
        );*/
    }
}

