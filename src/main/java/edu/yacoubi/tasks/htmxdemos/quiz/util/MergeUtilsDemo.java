package edu.yacoubi.tasks.htmxdemos.quiz.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demonstriert die Verwendung der MergeUtils-Klasse.
 * Zeigt, wie Map.merge() für Zählung, Text-Kombination und Gruppierung genutzt werden kann.
 */
public class MergeUtilsDemo {
    private static final Map<String, Integer> wordCountMap = new HashMap<>();
    private static final Map<String, String> tagMap = new HashMap<>();
    private static final Map<String, List<String>> groupMap = new HashMap<>();

    public static void main(String[] args) {
        // 🔢 Zählt, wie oft bestimmte Wörter vorkommen
        demoCounting();

        // 🏷️ Kombiniert Tags pro Schlüssel mit Komma-Trennung
        demoAppending();

        // 👥 Fügt mehrere Namen zu Gruppen hinzu
        demoGrouping();

        // 📊 Gibt die Ergebnisse der Map-Operationen aus
        printResults();

        // 🧹 Setzt alle Maps zurück
        resetAll();

        // 📭 Zeigt leere Maps nach dem Cleanup
        System.out.println("\n🧼 Nach resetAll():");
        printResults();
    }



    private static void demoCounting() {
        MergeUtils.count(wordCountMap, "Java");
        MergeUtils.count(wordCountMap, "Java");
        MergeUtils.count(wordCountMap, "Spring");
    }

    private static void demoAppending() {
        MergeUtils.appendText(tagMap, "dev", "Java");
        MergeUtils.appendText(tagMap, "dev", "Spring");
    }

    private static void demoGrouping() {
        MergeUtils.addToList(groupMap, "TeamA", new ArrayList<>(List.of("Alice")));
        MergeUtils.addToList(groupMap, "TeamA", new ArrayList<>(List.of("Bob", "Charlie")));

    }

    private static void printResults() {
        System.out.println("📊 Word Counts: " + wordCountMap);
        System.out.println("🏷️ Tags: " + tagMap);
        System.out.println("👥 Groups: " + groupMap);
    }

    private static void resetAll() {
        wordCountMap.clear();
        tagMap.clear();
        groupMap.clear();
    }
}
