package com.foi.nloncar.thesis_manager.repository;

import com.foi.nloncar.thesis_manager.model.ThesisSubmissionComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThesisSubmissionCommentRepository extends JpaRepository<ThesisSubmissionComment, Integer> {
	List<ThesisSubmissionComment> findBySubmissionIdOrderByCreatedAtAsc(Integer submissionId);
}
