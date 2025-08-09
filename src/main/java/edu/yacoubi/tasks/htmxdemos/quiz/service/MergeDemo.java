package edu.yacoubi.tasks.htmxdemos.quiz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lernmodul zur Demonstration von {@link Map#merge(Object, Object, java.util.function.BiFunction)}.
 * <p>
 * Was macht {@code Map.merge(K key, V value, BiFunction remappingFunction)}?
 * <ul>
 *     <li>Wenn der Schlüssel <b>nicht existiert</b>, wird {@code value} direkt eingefügt.</li>
 *     <li>Wenn der Schlüssel <b>existiert</b>, wird {@code remappingFunction} mit dem alten und neuen Wert aufgerufen,
 *         und das Ergebnis ersetzt den alten Wert.</li>
 * </ul>
 * <p>
 * Diese Klasse enthält gestaffelte Beispiele zur Verwendung von {@code merge()} mit Zahlen, Strings und Listen.
 * Die Beispiele sind gekapselt in privaten Methoden und können schrittweise aktiviert werden.
 */
public class MergeDemo {

    public static void main(String[] args) {
        System.out.println("🧪 MergeDemo gestartet");

        // Schritt 1: Integer-Zähler
        // runIntegerCounterExample();

        // Schritt 2: String-Kombination
        // runStringMergeExample();

        // Schritt 3: Liste pro Schlüssel
        // runListMergeExample();

        System.out.println("✅ MergeDemo abgeschlossen");
    }

    private static void runIntegerCounterExample() {
        System.out.println("\n🔍 Beispiel 1: Integer-Zähler mit merge()");
        Map<String, Integer> counterMap = new ConcurrentHashMap<>();

        counterMap.merge("sessionA", 1, Integer::sum);
        System.out.println("sessionA ➜ " + counterMap.get("sessionA")); // ➜ 1

        counterMap.merge("sessionA", 1, Integer::sum);
        System.out.println("sessionA ➜ " + counterMap.get("sessionA")); // ➜ 2
    }

    private static void runStringMergeExample() {
        System.out.println("\n🔍 Beispiel 2: String-Kombination mit eigener Remapping-Funktion");
        Map<String, String> messageMap = new HashMap<>();

        messageMap.merge("user1", "Hallo", (oldVal, newVal) -> oldVal + ", " + newVal);
        System.out.println("user1 ➜ " + messageMap.get("user1")); // ➜ Hallo

        messageMap.merge("user1", "wie geht's?", (oldVal, newVal) -> oldVal + ", " + newVal);
        System.out.println("user1 ➜ " + messageMap.get("user1")); // ➜ Hallo, wie geht's?
    }

    private static void runListMergeExample() {
        System.out.println("\n🔍 Beispiel 3: Liste pro Schlüssel mit merge()");
        Map<String, List<String>> listMap = new HashMap<>();

        listMap.merge("group1", new ArrayList<>(List.of("Alice")), (oldList, newList) -> {
            oldList.addAll(newList);
            return oldList;
        });
        System.out.println("group1 ➜ " + listMap.get("group1")); // ➜ [Alice]

        listMap.merge("group1", new ArrayList<>(List.of("Bob", "Charlie")), (oldList, newList) -> {
            oldList.addAll(newList);
            return oldList;
        });
        System.out.println("group1 ➜ " + listMap.get("group1")); // ➜ [Alice, Bob, Charlie]
    }
}
