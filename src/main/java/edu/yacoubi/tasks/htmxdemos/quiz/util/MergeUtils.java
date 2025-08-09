package edu.yacoubi.tasks.htmxdemos.quiz.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility-Klasse für erweiterte {@link Map#merge(Object, Object, java.util.function.BiFunction)}-Operationen.
 * <p>
 * Bietet Methoden zum Zählen, Kombinieren von Strings und Gruppieren von Listeneinträgen.
 */
public class MergeUtils {

    /**
     * Zählt, wie oft ein Schlüssel vorkommt.
     * <p>
     * Ideal für Wortzählung, Event-Tracking oder Häufigkeitsanalysen.
     *
     * @param map die Map mit Integer-Werten
     * @param key der Schlüssel, dessen Zählwert erhöht werden soll
     */
    public static void count(Map<String, Integer> map, String key) {
        map.merge(key, 1, Integer::sum);
    }

    /**
     * Kombiniert Strings pro Schlüssel mit Komma-Trennung.
     * <p>
     * Nützlich für Tags, Logs, Namenlisten oder Beschreibungstexte.
     *
     * @param map  die Map mit String-Werten
     * @param key  der Schlüssel, zu dem Text hinzugefügt werden soll
     * @param text der neue Text, der angehängt wird
     */
    public static void appendText(Map<String, String> map, String key, String text) {
        map.merge(key, text, (oldVal, newVal) -> oldVal + ", " + newVal);
    }

    /**
     * Fügt mehrere Elemente zu einer Liste pro Schlüssel hinzu.
     * <p>
     * Ideal für Gruppierungen, Sammlungen oder Zuordnungen.
     *
     * <p><strong>Hinweis:</strong> Die vorhandene Liste im Map-Eintrag muss veränderlich sein.
     * Wenn z. B. {@code List.of(...)} verwendet wurde, ist die Liste unveränderlich und
     * {@code addAll()} wirft eine {@link UnsupportedOperationException}.
     *
     * <p>Empfohlen: Verwende {@code new ArrayList<>(List.of(...))} für veränderliche Listen.
     *
     * @param map    die Map mit Listen als Werte
     * @param key    der Schlüssel, zu dem die Werte hinzugefügt werden sollen
     * @param values die neuen Werte, die hinzugefügt werden sollen
     * @param <K>    Typ des Schlüssels
     * @param <V>    Typ der Listenelemente
     */
    public static <K, V> void addToList(Map<K, List<V>> map, K key, List<V> values) {
        map.merge(key, new ArrayList<>(values), (oldList, newList) -> {
            oldList.addAll(newList);
            return oldList;
        });
    }
}
