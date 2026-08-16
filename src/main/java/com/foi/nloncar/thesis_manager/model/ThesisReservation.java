package com.foi.nloncar.thesis_manager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "thesis_reservation")
public class ThesisReservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "student_id", nullable = false)
	private User student;

	@ManyToOne
	@JoinColumn(name = "thesis_id", nullable = false)
	private Thesis thesis;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ThesisReservationStatus status;

	public ThesisReservation() {
	}

	public ThesisReservation(User student, Thesis thesis, ThesisReservationStatus status) {
		this.student = student;
		this.thesis = thesis;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public Thesis getThesis() {
		return thesis;
	}

	public void setThesis(Thesis thesis) {
		this.thesis = thesis;
	}

	public ThesisReservationStatus getStatus() {
		return status;
	}

	public void setStatus(ThesisReservationStatus status) {
		this.status = status;
	}
}
