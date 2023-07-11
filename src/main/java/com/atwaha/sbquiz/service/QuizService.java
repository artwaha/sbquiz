package com.atwaha.sbquiz.service;

import com.atwaha.sbquiz.DAO.QuestionRepository;
import com.atwaha.sbquiz.DAO.QuizRepository;
import com.atwaha.sbquiz.model.*;
import com.atwaha.sbquiz.util.Answer;
import com.atwaha.sbquiz.util.QuestionWrapper;
import com.atwaha.sbquiz.util.Score;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    public ResponseEntity<Quiz> addQuiz(String title, String category, int numOfQns) {
        try {
            if (isCategoryValid(category)) {
                List<Question> questions = questionRepository.generateRandomQuestionsByCategory(numOfQns, category);
                Quiz newQuiz = new Quiz();
                newQuiz.setTitle(title);
                newQuiz.setQuestions(questions);
                return ResponseEntity.status(HttpStatus.CREATED).body(quizRepository.save(newQuiz));
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private boolean isCategoryValid(String category) {
        List<String> availableCategories = questionRepository.findDistinctCategories();
        return availableCategories.contains(category);
    }

    public ResponseEntity<String> deleteQuiz(Long id) {
        try {
            quizRepository
                    .findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Quiz with ID " + id + " not found"));
            quizRepository.deleteById(id);
            return ResponseEntity.ok("Quiz with ID " + id + " deleted Successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<List<Quiz>> getQuizzes(Long id) {
        try {
            if (id == null)
                return ResponseEntity.ok(quizRepository.findAll());
            else {
                Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("invalid ID"));
                List<Quiz> quizzes = new ArrayList<>();
                quizzes.add(quiz);
                return ResponseEntity.ok(quizzes);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        }
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Long quizId) {
        try {
            Quiz quiz = quizRepository
                    .findById(quizId)
                    .orElseThrow(() -> new EntityNotFoundException("Quiz with ID" + quizId + " not found"));
            List<Question> quizQuestions = quiz.getQuestions();
            List<QuestionWrapper> questionsForUser = new ArrayList<>();
            for (Question question : quizQuestions) {
                QuestionWrapper questionWrapper = new QuestionWrapper(
                        question.getId(),
                        question.getQuestionTitle(),
                        question.getOption1(),
                        question.getOption2(),
                        question.getOption3(),
                        question.getOption4(),
                        question.getDifficultyLevel(),
                        question.getCategory()
                );
                questionsForUser.add(questionWrapper);
            }
            return ResponseEntity.ok(questionsForUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Score> submitQuiz(Long quizId, List<Answer> answers) {
        try {
            Quiz quiz = quizRepository.findById(quizId).
                    orElseThrow(() -> new EntityNotFoundException("Quiz with ID " + quizId + " not found"));
            List<Question> questions = quiz.getQuestions();
            int right = 0;

            for (Answer answer : answers) {
                Question qn = questions
                        .stream()
                        .filter(question -> question.getId().equals(answer.getQuestionId()))
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Question " + answer.getQuestionId() + " not found in Quiz " + quiz.getId()));
                if (answer.getResponse().equals(qn.getRightAnswer())) {
                    right++;
                }
            }
            return ResponseEntity.ok(new Score(right));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
