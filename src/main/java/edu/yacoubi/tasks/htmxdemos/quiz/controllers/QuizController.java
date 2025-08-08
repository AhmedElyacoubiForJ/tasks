package edu.yacoubi.tasks.htmxdemos.quiz.controllers;

import edu.yacoubi.tasks.htmxdemos.quiz.model.QuizQuestion;
import edu.yacoubi.tasks.htmxdemos.quiz.service.ProgressService;
import edu.yacoubi.tasks.htmxdemos.quiz.service.QuizService;
import edu.yacoubi.tasks.htmxdemos.quiz.service.ScoreService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;
    private final ProgressService progressService;
    private final ScoreService scoreService;

    @GetMapping
    public String showStartPage(final HttpSession session) {
        final String sessionId = session.getId();

        progressService.reset(sessionId);
        scoreService.reset(sessionId);

        return "htmxdemo/quiz/index";
    }

    @GetMapping("/next")
    public String loadNextQuestion(final HttpSession session, final Model model) {
        final String sessionId = session.getId();
        final int progress = progressService.get(sessionId);
        final QuizQuestion question = quizService.getQuestion(progress);

        if (question == null) {
            int score = scoreService.get(sessionId);
            int total = quizService.getTotalQuestions();

            model.addAttribute("score", score);
            model.addAttribute("total", total);
            return "htmxdemo/quiz/result";
        }

        model.addAttribute("question", question);
        model.addAttribute("progress", progress);
        model.addAttribute("total", quizService.getTotalQuestions());
        return "htmxdemo/quiz/question";
    }

    @PostMapping("/answer/check")
    public String checkAnswer(final @RequestParam String answer,
                              final HttpSession session, final Model model) {

        final String sessionId = session.getId();
        final int index = progressService.get(sessionId);
        final QuizQuestion question = quizService.getQuestion(index);

        final boolean isCorrect = question != null && question.isCorrect(answer);

        model.addAttribute("isCorrect", isCorrect);
        return "htmxdemo/quiz/feedback"; // kleines Fragment mit ✅ oder ❌
    }

    @PostMapping("/answer")
    public String submitAnswer(final @RequestParam String answer,
                               final HttpSession session, final Model model) {
        final String sessionId = session.getId();
        final int currentIndex = progressService.get(sessionId);
        final QuizQuestion currentQuestion = quizService.getQuestion(currentIndex);

        if (currentQuestion != null && currentQuestion.isCorrect(answer)) {
            scoreService.addPoint(sessionId); // Punkt vergeben
        }

        progressService.increment(sessionId); // Fortschritt erhöhen
        return loadNextQuestion(session, model); // Nächste Frage laden
    }
}
