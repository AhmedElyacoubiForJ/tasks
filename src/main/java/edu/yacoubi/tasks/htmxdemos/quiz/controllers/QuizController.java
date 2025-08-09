package edu.yacoubi.tasks.htmxdemos.quiz.controllers;

import edu.yacoubi.tasks.htmxdemos.quiz.model.QuizQuestion;
import edu.yacoubi.tasks.htmxdemos.quiz.service.ProgressService;
import edu.yacoubi.tasks.htmxdemos.quiz.service.QuizService;
import edu.yacoubi.tasks.htmxdemos.quiz.service.ScoreService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/quiz")
@RequiredArgsConstructor
@Slf4j
public class QuizController {
    private final QuizService quizService;
    private final ProgressService progressService;
    private final ScoreService scoreService;

    @GetMapping
    public String showStartPage(final HttpSession session) {
        final String sessionId = session.getId();

        log.info("Session gestartet: {}", sessionId);
        resetSessionState(sessionId);

        return "htmxdemo/quiz/index";
    }

    private void resetSessionState(String sessionId) {
        progressService.reset(sessionId);
        scoreService.reset(sessionId);
        log.debug("Progress und Score für Session {} zurückgesetzt", sessionId);
    }

    @GetMapping("/next")
    public String loadNextQuestion(final HttpSession session, final Model model) {
        final String sessionId = session.getId();
        final int progress = progressService.get(sessionId);
        final QuizQuestion question = quizService.getQuestion(progress);

        if (question == null) {
            return showResultPage(sessionId, model);
        }

        return showQuestionPage(question, progress, model);
    }

    private String showResultPage(final String sessionId, final Model model) {
        final int score = scoreService.get(sessionId);
        final int total = quizService.getTotalQuestions();

        model.addAttribute("score", score);
        model.addAttribute("total", total);
        return "htmxdemo/quiz/result";
    }

    private String showQuestionPage(final QuizQuestion question,
                                    final int progress, final Model model) {

        if (question.hasCode()) {
            List<String> codeLines = Arrays.stream(question.getCodeText().split(";"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(s -> s + ";")
                    .collect(Collectors.toList());
            model.addAttribute("codeLines", codeLines);
            codeLines.stream().forEach(System.out::println);
            System.out.println("codeLines.size() : " + codeLines.size());
        }

        //model.addAttribute("codeLines", codeLines);

        model.addAttribute("question", question);
        model.addAttribute("progress", progress);
        model.addAttribute("total", quizService.getTotalQuestions());
        return "htmxdemo/quiz/question";
    }

    @PostMapping("/answer/check")
    public String checkAnswer(@RequestParam final String answer,
                              final HttpSession session,
                              final Model model) {

        final String sessionId = session.getId();
        final boolean isCorrect = evaluateAnswer(sessionId, answer);

        model.addAttribute("isCorrect", isCorrect);
        return "htmxdemo/quiz/feedback";
    }

    private boolean evaluateAnswer(final String sessionId, final String answer) {
        final int index = progressService.get(sessionId);
        final QuizQuestion question = quizService.getQuestion(index);

        return question != null && question.isCorrect(answer);
    }

    @PostMapping("/answer")
    public String submitAnswer(@RequestParam final String answer,
                               final HttpSession session,
                               final Model model) {

        final String sessionId = session.getId();
        processAnswer(sessionId, answer);
        progressService.increment(sessionId);

        return loadNextQuestion(session, model);
    }

    private void processAnswer(final String sessionId, final String answer) {
        final int currentIndex = progressService.get(sessionId);
        final QuizQuestion currentQuestion = quizService.getQuestion(currentIndex);

        if (currentQuestion != null && currentQuestion.isCorrect(answer)) {
            scoreService.addPoint(sessionId);
            log.debug("Richtige Antwort für Frage {} – Punkt vergeben", currentIndex);
        } else {
            log.debug("Falsche oder keine Antwort für Frage {}", currentIndex);
        }
    }
}
