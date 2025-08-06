package edu.yacoubi.tasks.htmxdemos.quiz.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class QuizQuestion {
    private String questionText;
    private List<String> options;
    private String correctAnswer;

    public boolean isCorrect(String answer) {
        return correctAnswer.equals(answer);
    }
}
