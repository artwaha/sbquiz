package com.atwaha.sbquiz.controller;

import com.atwaha.sbquiz.util.Answer;
import com.atwaha.sbquiz.util.QuestionWrapper;
import com.atwaha.sbquiz.model.Quiz;
import com.atwaha.sbquiz.util.Score;
import com.atwaha.sbquiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    //    If Id is not passed, fetch all quizzes
    @GetMapping
    ResponseEntity<List<Quiz>> getQuizzes(@RequestParam(required = false) Long id) {
        return quizService.getQuizzes(id);
    }

    @GetMapping("{quizId}/questions")
    ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Long quizId) {
        return quizService.getQuizQuestions(quizId);
    }

    //    Submit Answer
    @PostMapping("{quizId}")
    ResponseEntity<Score> submitQuiz(@PathVariable Long quizId, @RequestBody List<Answer> answers) {
        return quizService.submitQuiz(quizId, answers);
    }


    @PostMapping
    ResponseEntity<Quiz> addQuiz(@RequestParam String title, @RequestParam String category, @RequestParam int numOfQns) {
        return quizService.addQuiz(title, category, numOfQns);
    }


    @DeleteMapping
    ResponseEntity<String> deleteQuiz(@RequestParam Long id) {
        return quizService.deleteQuiz(id);
    }
}
