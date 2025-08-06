package edu.yacoubi.tasks.htmxdemos.quiz.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Verwalten der aktuellen Frage
// Fokus auf Ablaufsteuerung
@Service
public class ProgressService {
    private final Map<String, Integer> progressMap = new ConcurrentHashMap<>();

    public void reset(String sessionId) {
        progressMap.put(sessionId, 0);
    }

    public int get(String sessionId) {
        return progressMap.getOrDefault(sessionId, 0);
    }

    public void increment(String sessionId) {
        progressMap.merge(sessionId, 1, Integer::sum);
    }
}
