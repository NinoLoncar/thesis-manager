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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "thesis_submission")
public class ThesisSubmission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "thesis_id", nullable = false)
	private Thesis thesis;

	@ManyToOne
	@JoinColumn(name = "student_id", nullable = false)
	private User student;

	@Column(nullable = false)
	private Integer version;

	@Column(name = "file_path", nullable = false)
	private String filePath;

	@Column(name = "file_name", nullable = false)
	private String fileName;

	@Column
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ThesisSubmissionStatus status;

	@ManyToOne
	@JoinColumn(name = "reviewed_by")
	private User reviewedBy;

	@Column(name = "reviewed_at")
	private LocalDateTime reviewedAt;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	public ThesisSubmission() {
	}

	public ThesisSubmission(Thesis thesis, User student, Integer version, String filePath, String fileName,
							 String description, ThesisSubmissionStatus status) {
		this.thesis = thesis;
		this.student = student;
		this.version = version;
		this.filePath = filePath;
		this.fileName = fileName;
		this.description = description;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Thesis getThesis() {
		return thesis;
	}

	public void setThesis(Thesis thesis) {
		this.thesis = thesis;
	}

	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ThesisSubmissionStatus getStatus() {
		return status;
	}

	public void setStatus(ThesisSubmissionStatus status) {
		this.status = status;
	}

	public User getReviewedBy() {
		return reviewedBy;
	}

	public void setReviewedBy(User reviewedBy) {
		this.reviewedBy = reviewedBy;
	}

	public LocalDateTime getReviewedAt() {
		return reviewedAt;
	}

	public void setReviewedAt(LocalDateTime reviewedAt) {
		this.reviewedAt = reviewedAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}
}
