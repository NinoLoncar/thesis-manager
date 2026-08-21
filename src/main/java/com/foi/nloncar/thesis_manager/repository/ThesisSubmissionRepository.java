package com.foi.nloncar.thesis_manager.repository;

import com.foi.nloncar.thesis_manager.model.ThesisSubmission;
import com.foi.nloncar.thesis_manager.model.ThesisSubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThesisSubmissionRepository extends JpaRepository<ThesisSubmission, Integer> {
	List<ThesisSubmission> findByThesisIdOrderByVersionDesc(Integer thesisId);

	boolean existsByThesisIdAndStatus(Integer thesisId, ThesisSubmissionStatus status);
}
