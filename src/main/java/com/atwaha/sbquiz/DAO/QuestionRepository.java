package com.atwaha.sbquiz.DAO;

import com.atwaha.sbquiz.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCategory(String category);

    @Query("SELECT DISTINCT category FROM Question")
    List<String> findDistinctCategories();

    @Query(
//            value = "SELECT DISTINCT q.category FROM questions q WHERE q.category = :category ORDER BY RANDOM() LIMIT :numOfQns",
            value = "SELECT * FROM questions q WHERE q.category=:category ORDER BY RANDOM() LIMIT :numOfQns",
            nativeQuery = true
    )
    List<Question> generateRandomQuestionsByCategory(int numOfQns, String category);
}
