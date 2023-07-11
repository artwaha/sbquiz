package com.atwaha.sbquiz.DAO;

import com.atwaha.sbquiz.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
