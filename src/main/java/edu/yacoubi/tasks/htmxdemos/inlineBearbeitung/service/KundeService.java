package edu.yacoubi.tasks.htmxdemos.inlineBearbeitung.service;

import edu.yacoubi.tasks.htmxdemos.inlineBearbeitung.model.Kunde;
import edu.yacoubi.tasks.htmxdemos.inlineBearbeitung.repository.KundeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class KundeService {
    private final KundeRepository repo;

    public List<Kunde> findAll() {
        return repo.findAll();
    }

    public Kunde findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Kunde update(Long id, Kunde kunde) {
        return repo.update(id, kunde);
    }
}
