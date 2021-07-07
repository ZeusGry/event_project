package com.example.event_project.repository;

import com.example.event_project.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentByEvent_IdOrderByPostTime(Long id);
}
