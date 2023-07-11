package com.atwaha.sbquiz.controller;

import com.atwaha.sbquiz.model.Question;
import com.atwaha.sbquiz.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/questions")
public class QuestionController {
    private final QuestionService questionService;

    //    If no category is passed, fetches all questions
    @GetMapping
    ResponseEntity<List<Question>> getQuestions(@RequestParam(required = false) String category) {
        return questionService.getQuestions(category);
    }

    //    Fetch question by id
    @GetMapping("{id}")
    ResponseEntity<Question> getQuestion(@PathVariable Long id) {
        return questionService.getQuestion(id);
    }

    //Create new Question
    @PostMapping
    ResponseEntity<Question> addQuestion(@RequestBody Question question) {
        return questionService.addQuestion(question);
    }

    @DeleteMapping
    ResponseEntity<String> deleteQuestion(@RequestParam Long id) {
        return questionService.deleteQuestion(id);
    }
}
