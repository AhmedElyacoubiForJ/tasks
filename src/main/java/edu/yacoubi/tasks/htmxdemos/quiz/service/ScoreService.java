package edu.yacoubi.tasks.htmxdemos.quiz.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Zählen der richtigen Antworten
// Fokus auf Bewertung & Ergebnis
@Service
public class ScoreService {

    private final Map<String, Integer> scoreMap = new ConcurrentHashMap<>();

    public int get(String sessionId) {
        return scoreMap.getOrDefault(sessionId, 0);
    }

    public void addPoint(String sessionId) {
        scoreMap.merge(sessionId, 1, Integer::sum);
    }

    public void reset(String sessionId) {
        scoreMap.put(sessionId, 0);
    }
}

