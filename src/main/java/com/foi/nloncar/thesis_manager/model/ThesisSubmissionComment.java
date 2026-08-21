package com.foi.nloncar.thesis_manager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "thesis_submission_comment")
public class ThesisSubmissionComment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "submission_id", nullable = false)
	private ThesisSubmission submission;

	@ManyToOne
	@JoinColumn(name = "author_id", nullable = false)
	private User author;

	@Column(nullable = false)
	private String content;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	public ThesisSubmissionComment() {
	}

	public ThesisSubmissionComment(ThesisSubmission submission, User author, String content) {
		this.submission = submission;
		this.author = author;
		this.content = content;
	}

	public Integer getId() {
		return id;
	}

	public ThesisSubmission getSubmission() {
		return submission;
	}

	public User getAuthor() {
		return author;
	}

	public String getContent() {
		return content;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}
}
