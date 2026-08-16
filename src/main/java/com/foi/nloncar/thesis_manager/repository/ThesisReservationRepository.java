package com.foi.nloncar.thesis_manager.repository;

import com.foi.nloncar.thesis_manager.model.ThesisReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThesisReservationRepository extends JpaRepository<ThesisReservation, Integer> {
	List<ThesisReservation> findByThesisId(Integer thesisId);
}
