package edu.yacoubi.tasks.htmxdemos.quiz.util;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MergeUtilsTest {

    @Test
    void itShouldCountOccurrencesCorrectly() {
        Map<String, Integer> map = new HashMap<>();
        MergeUtils.count(map, "Java");
        MergeUtils.count(map, "Java");
        MergeUtils.count(map, "Spring");

        assertEquals(2, map.get("Java"));
        assertEquals(1, map.get("Spring"));
    }

    @Test
    void itShouldAppendTextWithCommaSeparation() {
        Map<String, String> map = new HashMap<>();
        MergeUtils.appendText(map, "dev", "Java");
        MergeUtils.appendText(map, "dev", "Spring");

        assertEquals("Java, Spring", map.get("dev"));
    }

    @Test
    void itShouldAddMultipleItemsToListPerKey() {
        Map<String, List<String>> map = new HashMap<>();
        MergeUtils.addToList(map, "TeamA", List.of("Alice"));
        MergeUtils.addToList(map, "TeamA", List.of("Bob", "Charlie"));

        List<String> expected = List.of("Alice", "Bob", "Charlie");
        assertEquals(expected, map.get("TeamA"));
    }

    @Test
    void itShouldThrowExceptionWhenMergingIntoImmutableList() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("TeamB", List.of("Zoe")); // unveränderlich

        assertThrows(UnsupportedOperationException.class, () ->
                map.merge("TeamB", List.of("Liam"), (oldList, newList) -> {
                    oldList.addAll(newList); // 💥
                    return oldList;
                })
        );
    }
}
