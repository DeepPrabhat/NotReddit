package com.project.reddit.repositories;

import com.project.reddit.models.Comment;
import jakarta.validation.ReportAsSingleViolation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
}
