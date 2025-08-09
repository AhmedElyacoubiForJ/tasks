package edu.yacoubi.tasks.htmxdemos.quiz.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@EqualsAndHashCode
@Getter
public class QuizQuestion {
    private final String questionText;
    private final String codeText;
    private final List<String> options;
    private final String correctAnswer;
    private final QuestionType type;

    // Konstruktor für reine Textfragen
    public QuizQuestion(String questionText, List<String> options,
                        String correctAnswer) {
        this.questionText = questionText;
        this.codeText = "";
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.type = QuestionType.TEXT_ONLY;
    }

    // Konstruktor für Fragen mit Code
    public QuizQuestion(String questionText, String codeText,
                        List<String> options, String correctAnswer) {
        this.questionText = questionText;
        this.codeText = codeText;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.type = QuestionType.TEXT_WITH_CODE;
    }

    public boolean isCorrect(String answer) {
        return correctAnswer.equals(answer);
    }

    public boolean hasCode() {
        return codeText != null && !codeText.trim().isEmpty();
    }

    public enum QuestionType {
        TEXT_ONLY,
        TEXT_WITH_CODE
    }
}
