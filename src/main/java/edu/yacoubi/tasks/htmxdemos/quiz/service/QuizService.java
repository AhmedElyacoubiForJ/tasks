package edu.yacoubi.tasks.htmxdemos.quiz.service;

import edu.yacoubi.tasks.htmxdemos.quiz.model.QuizQuestion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    private final List<QuizQuestion> quizQuestions = List.of(
            new QuizQuestion("Was ist die Hauptstadt von Frankreich?", List.of("Paris", "Berlin", "Rom"), "Paris"),
            new QuizQuestion("Was ist 2 + 2?", List.of("3", "4", "5"), "4"),
            new QuizQuestion("Welche Farbe hat der Himmel?", List.of("Blau", "Grün", "Rot"), "Blau")
    );

    public QuizQuestion getQuestion(int index) {
        return index < quizQuestions.size() ? quizQuestions.get(index) : null;
    }

    public int getTotalQuestions() {
        return quizQuestions.size();
    }
}
