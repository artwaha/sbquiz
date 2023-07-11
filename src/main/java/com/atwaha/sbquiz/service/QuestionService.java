package com.atwaha.sbquiz.service;

import com.atwaha.sbquiz.DAO.QuestionRepository;
import com.atwaha.sbquiz.model.Question;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public ResponseEntity<List<Question>> getQuestions(String category) {
        try {
            if (category == null)
                return ResponseEntity.ok(questionRepository.findAll());
            else {
                if (isCategoryValid(category))
                    return ResponseEntity.ok(questionRepository.findByCategory(category));
                else // Category is not valid - would you rather throw an exception?
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private boolean isCategoryValid(String category) {
        List<String> validCategories = questionRepository.findDistinctCategories();
        return validCategories.contains(category);
    }

    public ResponseEntity<Question> addQuestion(Question question) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(questionRepository.save(question));
        } catch (Exception e) {
            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorObject());
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<String> deleteQuestion(Long id) {
        try {
            questionRepository
                    .findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("ID " + id + " not found"));
            questionRepository.deleteById(id);
            return ResponseEntity.ok("Question with ID " + id + " deleted successfully");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    //    Get question by id
    public ResponseEntity<Question> getQuestion(Long id) {
        try {
            Question question = questionRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Id not found"));
            return ResponseEntity.ok(question);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
