package edu.yacoubi.tasks.htmxdemos.quiz.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service zur Verwaltung des Fortschritts eines Benutzers im Quiz.
 * <p>
 * Jeder Benutzer wird über seine Session-ID identifiziert.
 * Der Fortschritt wird als Index der aktuellen Frage gespeichert.
 * Thread-sicher durch Verwendung von {@link ConcurrentHashMap}.
 */
@Service
@Slf4j
public class ProgressService {

    /**
     * Map zur Speicherung des Fortschritts pro Session.
     * Schlüssel: Session-ID, Wert: Index der aktuellen Frage.
     */
    private final Map<String, Integer> progressMap = new ConcurrentHashMap<>();

    /**
     * Setzt den Fortschritt für eine Session auf 0 (Quizstart).
     *
     * @param sessionId die eindeutige ID der Session
     */
    public void reset(String sessionId) {
        progressMap.put(sessionId, 0);
    }

    /**
     * Gibt den aktuellen Fortschritt (Frageindex) für eine Session zurück.
     * Falls keine Eintragung vorhanden ist, wird 0 zurückgegeben.
     *
     * @param sessionId die eindeutige ID der Session
     * @return aktueller Frageindex
     */
    public int get(String sessionId) {
        return progressMap.getOrDefault(sessionId, 0);
    }

    /**
     * Erhöht den Fortschritt für eine Session um 1.
     * <p>
     * Intern wird {@link Map#merge(Object, Object, java.util.function.BiFunction)} verwendet,
     * um den aktuellen Wert atomar zu erhöhen. Falls kein Eintrag existiert, wird der Fortschritt mit 1 initialisiert.
     * Diese Methode wird typischerweise nach dem erfolgreichen Abschluss einer Frage aufgerufen.
     *
     * @param sessionId die eindeutige ID der Session
     */
    public void increment(String sessionId) {
        // Wenn kein Wert vorhanden ist, wird 1 gesetzt.
        // Wenn bereits ein Wert existiert, wird 1 addiert.
        // merge(sessionId, 1, Integer::sum) entspricht:
        // progressMap.put(sessionId, progressMap.get(sessionId) + 1)
        progressMap.merge(sessionId, 1, Integer::sum);
        log.debug("Fortschritt für Session {} erhöht auf {}", sessionId, get(sessionId));
    }

}
