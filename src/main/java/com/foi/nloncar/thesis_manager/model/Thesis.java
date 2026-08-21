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

import java.time.LocalDateTime;

@Entity
@Table(name = "thesis")
public class Thesis {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String title;

	@Column(name = "abstract")
	private String abstractText;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ThesisType type;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ThesisStatus status;

	@ManyToOne
	@JoinColumn(name = "student_id")
	private User student;

	@ManyToOne
	@JoinColumn(name = "mentor_id", nullable = false)
	private User mentor;

	@Column(name = "submitted_at")
	private LocalDateTime submittedAt;

	@Column(name = "reserved_at")
	private LocalDateTime reservedAt;

	public Thesis() {
	}

	public Thesis(String title, String abstractText, ThesisType type, ThesisStatus status, User student, User mentor) {
		this.title = title;
		this.abstractText = abstractText;
		this.type = type;
		this.status = status;
		this.student = student;
		this.mentor = mentor;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAbstractText() {
		return abstractText;
	}

	public void setAbstractText(String abstractText) {
		this.abstractText = abstractText;
	}

	public ThesisType getType() {
		return type;
	}

	public void setType(ThesisType type) {
		this.type = type;
	}

	public ThesisStatus getStatus() {
		return status;
	}

	public void setStatus(ThesisStatus status) {
		this.status = status;
	}

	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public User getMentor() {
		return mentor;
	}

	public void setMentor(User mentor) {
		this.mentor = mentor;
	}

	public LocalDateTime getSubmittedAt() {
		return submittedAt;
	}

	public void setSubmittedAt(LocalDateTime submittedAt) {
		this.submittedAt = submittedAt;
	}

	public LocalDateTime getReservedAt() {
		return reservedAt;
	}

	public void setReservedAt(LocalDateTime reservedAt) {
		this.reservedAt = reservedAt;
	}
}
