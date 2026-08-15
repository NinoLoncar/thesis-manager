package com.foi.nloncar.thesis_manager.repository;

import com.foi.nloncar.thesis_manager.model.Thesis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThesisRepository extends JpaRepository<Thesis, Integer> {
}
