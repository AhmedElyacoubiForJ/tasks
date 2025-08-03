package edu.yacoubi.tasks.htmxdemos.inlineBearbeitung.repository;

import edu.yacoubi.tasks.htmxdemos.inlineBearbeitung.model.Kunde;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class KundeRepository {

    private final List<Kunde> kunden = new ArrayList<>();
    private Long nextId = 1L;

    public KundeRepository() {
        kunden.add(new Kunde(nextId++, "Max Mustermann", "max@beispiel.de"));
        kunden.add(new Kunde(nextId++, "Sofia Schneider", "sofia@beispiel.de"));
        kunden.add(new Kunde(nextId++, "Tim Becker", "tim@beispiel.de"));
    }

    public List<Kunde> findAll() {
        return kunden;
    }

    public Optional<Kunde> findById(Long id) {
        return kunden.stream()
                .filter(k -> k.getId().equals(id))
                .findFirst();
    }

    public Kunde update(Long id, Kunde updatedKunde) {
        return findById(id).map(existing -> {
            existing.setName(updatedKunde.getName());
            existing.setEmail(updatedKunde.getEmail());
            return existing;
        }).orElse(null);
    }
}

