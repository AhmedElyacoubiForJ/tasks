package edu.yacoubi.tasks.htmxdemos.quiz.service;

import edu.yacoubi.tasks.htmxdemos.quiz.model.QuizQuestion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    //    private final List<QuizQuestion> quizQuestions = List.of(
//            // Entwickler-Quiz zu Map.merge()
//            new QuizQuestion(
//                    "Was macht Map.merge(key, value, remappingFunction) in Java?",
//                    List.of(
//                            "Fügt einen neuen Eintrag hinzu, wenn der Schlüssel nicht existiert, oder kombiniert den alten und neuen Wert",
//                            "Überschreibt immer den alten Wert mit dem neuen",
//                            "Löscht den Eintrag, wenn der Schlüssel existiert"
//                    ),
//                    "Fügt einen neuen Eintrag hinzu, wenn der Schlüssel nicht existiert, oder kombiniert den alten und neuen Wert"
//            ),
//            new QuizQuestion(
//                    "Welche Funktion erfüllt die remappingFunction in Map.merge()?",
//                    List.of(
//                            "Sie entscheidet, ob der Schlüssel gelöscht wird",
//                            "Sie kombiniert alten und neuen Wert",
//                            "Sie validiert den Schlüssel"
//                    ),
//                    "Sie kombiniert alten und neuen Wert"
//            ),
//            new QuizQuestion(
//                    "Was passiert, wenn die remappingFunction null zurückgibt?",
//                    List.of(
//                            "Der Eintrag wird gelöscht",
//                            "Der neue Wert wird gespeichert",
//                            "Es wird eine Exception geworfen"
//                    ),
//                    "Der Eintrag wird gelöscht"
//            ),
//            new QuizQuestion(
//                    "Welche Alternative zu Map.merge() gibt es, um Werte zu aktualisieren?",
//                    List.of(
//                            "putIfAbsent()",
//                            "compute()",
//                            "replaceAll()"
//                    ),
//                    "compute()"
//            ),
//            new QuizQuestion(
//                    "Was ist die Ausgabe dieses Codes?",
//                    "Map<String, Integer> map = new HashMap<>();map.put(\"A\", 1);map.merge(\"A\", 2, Integer::sum);System.out.println(map.get(\"A\"));",
//                    List.of("2", "3", "1"),
//                    "3"
//            ),
//            new QuizQuestion(
//                    "Was passiert hier?",
//                    "Map<String, String> map = new HashMap<>();map.put(\"X\", \"Hallo\");map.merge(\"X\", \"\", (oldVal, newVal) -> null);System.out.println(map.containsKey(\"X\"));",
//                    List.of("true", "false", "Exception"),
//                    "false"
//            ),
//            new QuizQuestion(
//                    "Welche Aussage trifft auf diesen Code zu?",
//                    "Map<String, String> map = new HashMap<>();map.merge(\"Y\", \"Start\", (oldVal, newVal) -> oldVal + newVal);",
//                    List.of(
//                            "Ein neuer Eintrag mit Schlüssel 'Y' und Wert 'Start' wird hinzugefügt",
//                            "Der alte Wert wird gelöscht",
//                            "Der Code wirft eine NullPointerException"
//                    ),
//                    "Ein neuer Eintrag mit Schlüssel 'Y' und Wert 'Start' wird hinzugefügt"
//            ),
//            new QuizQuestion(
//                    "Was ist die Ausgabe?",
//                    "Map<String, Integer> map = new HashMap<>();map.merge(\"Z\", 5, (oldVal, newVal) -> oldVal * newVal);map.merge(\"Z\", 2, (oldVal, newVal) -> oldVal * newVal);System.out.println(map.get(\"Z\"));",
//                    List.of("10", "5", "2"),
//                    "10"
//            ),
//            new QuizQuestion(
//                    "Fill in the blank:",
//                    "Map<String, Integer> map = new HashMap<>();map.merge(\"count\", 1, (oldVal, newVal) -> ___);Was muss in die Lücke, um die Werte zu addieren?",
//                    List.of("oldVal + newVal", "newVal", "oldVal - newVal"),
//                    "oldVal + newVal"
//            ),
//            new QuizQuestion(
//                    "Was ist die Ausgabe?",
//                    "Map<String, String> map = new HashMap<>();map.merge(\"lang\", \"Java\", (oldVal, newVal) -> oldVal.toUpperCase() + \" & \" + newVal);System.out.println(map.get(\"lang\"));",
//                    List.of("JAVA & Java", "Java", "java & Java"),
//                    "JAVA & Java"
//            ),
//            new QuizQuestion(
//                    "Was passiert, wenn der Schlüssel nicht existiert?",
//                    "Map<String, Integer> map = new HashMap<>();map.merge(\"score\", 10, (oldVal, newVal) -> oldVal + newVal);System.out.println(map.get(\"score\"));",
//                    List.of("10", "0", "Exception"),
//                    "10"
//            )
//    );
    // Quiz-Kategorien. zu Aggregationen, Subqueries oder Window Functions kommen später.
    private final List<QuizQuestion> quizQuestions = List.of(
            new QuizQuestion(
                    "Welche SQL-Abfrage zeigt alle Task-Listen, auch wenn keine Tasks zugeordnet sind?",
                    "SELECT * FROM task_lists LEFT JOIN tasks ON task_lists.id = tasks.task_list_id;",
                    List.of(
                            "LEFT JOIN",
                            "INNER JOIN",
                            "RIGHT JOIN",
                            "FULL JOIN"
                    ),
                    "LEFT JOIN"
            ),
            new QuizQuestion(
                    "Welche JOIN-Abfrage zeigt nur Tasks, die einer existierenden Liste zugeordnet sind?",
                    "SELECT * FROM tasks INNER JOIN task_lists ON tasks.task_list_id = task_lists.id;",
                    List.of(
                            "INNER JOIN",
                            "LEFT JOIN",
                            "RIGHT JOIN",
                            "FULL JOIN"
                    ),
                    "INNER JOIN"
            ),
            new QuizQuestion(
                    "Welche JOIN-Abfrage zeigt alle Tasks, auch wenn keine zugehörige Liste existiert?",
                    "SELECT * FROM tasks LEFT JOIN task_lists ON tasks.task_list_id = task_lists.id;",
                    List.of(
                            "LEFT JOIN",
                            "INNER JOIN",
                            "RIGHT JOIN",
                            "FULL JOIN"
                    ),
                    "LEFT JOIN"
            ),
            new QuizQuestion(
                    "Welche JOIN-Abfrage erzeugt jede mögliche Kombination aus Tasks und Listen?",
                    "SELECT * FROM tasks CROSS JOIN task_lists;",
                    List.of(
                            "CROSS JOIN",
                            "INNER JOIN",
                            "LEFT JOIN",
                            "FULL JOIN"
                    ),
                    "CROSS JOIN"
            ),
            new QuizQuestion(
                    "Welche JOIN-Abfrage zeigt alle Tasks und alle Listen, auch wenn keine Verbindung besteht?",
                    "SELECT * FROM tasks FULL JOIN task_lists ON tasks.task_list_id = task_lists.id;",
                    List.of(
                            "FULL JOIN",
                            "INNER JOIN",
                            "LEFT JOIN",
                            "RIGHT JOIN"
                    ),
                    "FULL JOIN"
            ),
            new QuizQuestion(
                    "Welche JOIN-Abfrage zeigt alle Listen, auch wenn keine Tasks vorhanden sind?",
                    "SELECT task_lists.title, tasks.title FROM task_lists LEFT JOIN tasks ON task_lists.id = tasks.task_list_id;",
                    List.of(
                            "LEFT JOIN",
                            "RIGHT JOIN",
                            "INNER JOIN",
                            "CROSS JOIN"
                    ),
                    "LEFT JOIN"
            ),
            new QuizQuestion(
                    "Welche JOIN-Abfrage zeigt Tasks, die keiner Liste zugeordnet sind?",
                    "SELECT tasks.title FROM tasks LEFT JOIN task_lists ON tasks.task_list_id = task_lists.id WHERE task_lists.id IS NULL;",
                    List.of(
                            "LEFT JOIN mit WHERE task_lists.id IS NULL",
                            "INNER JOIN mit WHERE task_lists.id IS NULL",
                            "RIGHT JOIN mit WHERE task_lists.id IS NULL",
                            "FULL JOIN mit WHERE task_lists.id IS NULL"
                    ),
                    "LEFT JOIN mit WHERE task_lists.id IS NULL"
            ),
            new QuizQuestion(
                    "Welche JOIN-Abfrage zeigt Listen, die keine Tasks enthalten?",
                    "SELECT task_lists.title FROM task_lists LEFT JOIN tasks ON task_lists.id = tasks.task_list_id WHERE tasks.id IS NULL;",
                    List.of(
                            "LEFT JOIN mit WHERE tasks.id IS NULL",
                            "INNER JOIN mit WHERE tasks.id IS NULL",
                            "RIGHT JOIN mit WHERE tasks.id IS NULL",
                            "FULL JOIN mit WHERE tasks.id IS NULL"
                    ),
                    "LEFT JOIN mit WHERE tasks.id IS NULL"
            ),
            new QuizQuestion(
                    "Welche JOIN-Abfrage zeigt alle Kombinationen, aber ohne Join-Bedingung?",
                    "SELECT * FROM tasks CROSS JOIN task_lists;",
                    List.of(
                            "CROSS JOIN",
                            "INNER JOIN",
                            "LEFT JOIN",
                            "RIGHT JOIN"
                    ),
                    "CROSS JOIN"
            ),
            new QuizQuestion(
                    "Welche JOIN-Abfrage zeigt Tasks mit ihren Listen, sortiert nach Listen-Titel?",
                    "SELECT tasks.title, task_lists.title FROM tasks INNER JOIN task_lists ON tasks.task_list_id = task_lists.id ORDER BY task_lists.title;",
                    List.of(
                            "INNER JOIN mit ORDER BY",
                            "LEFT JOIN mit ORDER BY",
                            "RIGHT JOIN mit ORDER BY",
                            "FULL JOIN mit ORDER BY"
                    ),
                    "INNER JOIN mit ORDER BY"
            )
    );

    public QuizQuestion getQuestion(int index) {
        return index < quizQuestions.size() ? quizQuestions.get(index) : null;
    }

    public int getTotalQuestions() {
        return quizQuestions.size();
    }
}