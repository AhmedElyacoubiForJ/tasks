package edu.yacoubi.tasks.htmxdemos.quiz.controllers;

import edu.yacoubi.tasks.htmxdemos.quiz.model.QuizQuestion;
import edu.yacoubi.tasks.htmxdemos.quiz.service.ProgressService;
import edu.yacoubi.tasks.htmxdemos.quiz.service.ScoreService;
import edu.yacoubi.tasks.htmxdemos.quiz.service.QuizService;
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
    public String showStartPage(HttpSession session) {
        String sessionId = session.getId();

        progressService.reset(sessionId);
        scoreService.reset(sessionId);

        return "htmxdemo/quiz/index";
    }

    @GetMapping("/next")
    public String loadNextQuestion(HttpSession session, Model model) {
        String sessionId = session.getId();
        int progress = progressService.get(sessionId);
        QuizQuestion question = quizService.getQuestion(progress);

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


    @PostMapping("/answer")
    public String submitAnswer(@RequestParam String answer, HttpSession session, Model model) {
        String sessionId = session.getId();
        int currentIndex = progressService.get(sessionId);
        QuizQuestion currentQuestion = quizService.getQuestion(currentIndex);

        if (currentQuestion != null && currentQuestion.isCorrect(answer)) {
            scoreService.addPoint(sessionId); // Punkt vergeben
        }

        progressService.increment(sessionId); // Fortschritt erhöhen
        return loadNextQuestion(session, model); // Nächste Frage laden
    }
}
