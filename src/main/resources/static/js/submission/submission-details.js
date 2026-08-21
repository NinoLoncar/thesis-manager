let currentSubmission = null;

$(function () {
	loadSubmission();
	loadComments();

	$('#comment-submit-btn').on('click', function (event) {
		event.preventDefault();
		submitComment();
	});

	$('#request-changes-btn').on('click', function () {
		reviewSubmission('request-changes');
	});

	$('#accept-btn').on('click', function () {
		if (confirm('This will mark the thesis as submitted and no further versions will be accepted. Accept this version as the final one?')) {
			reviewSubmission('accept');
		}
	});
});

function loadSubmission() {
	$.ajax({
		url: '/api/submissions/' + submissionId,
		method: 'GET',
		success: function (submission) {
			currentSubmission = submission;
			renderSubmission(submission);
			checkThesisOwnership(submission.thesisId);
		},
		error: function (jqXHR) {
			showError(jqXHR.responseJSON.message);
		}
	});
}

function renderSubmission(submission) {
	$('#submission-title').text('Version ' + submission.version);
	$('#submission-version').text(submission.version);
	$('#submission-file').text(submission.fileName);
	$('#submission-description').text(submission.description || '-');
	$('#submission-status').text(submission.status);
	$('#submission-student').text(submission.studentName);
	$('#submission-reviewer').text(submission.reviewedByName || '-');

	$('#download-link').attr('href', '/api/submissions/' + submissionId + '/download');
}

function checkThesisOwnership(thesisId) {
	$.ajax({
		url: '/api/theses/' + thesisId,
		method: 'GET',
		success: function (thesis) {
			const isOwner = thesis.mentorId === currentUserId || thesis.studentId === currentUserId;
			$('#comment-form').toggleClass('d-none', !(canCreateComment && isOwner));

			const isMentor = thesis.mentorId === currentUserId;
			const canReviewNow = canReviewSubmission && isMentor && currentSubmission.status === 'UNDER_REVIEW';
			$('#review-actions').toggleClass('d-none', !canReviewNow);
		},
		error: function (jqXHR) {
			showError(jqXHR.responseJSON.message);
		}
	});
}

function reviewSubmission(action) {
	$.ajax({
		url: '/api/submissions/' + submissionId + '/' + action,
		method: 'PUT'
	})
		.done(function () {
			loadSubmission();
		})
		.fail(function (jqXHR) {
			showError(jqXHR.responseJSON.message);
		});
}

function loadComments() {
	$.ajax({
		url: '/api/submission-comments',
		method: 'GET',
		data: {submissionId: submissionId},
		success: function (comments) {
			renderComments(comments);
		},
		error: function (jqXHR) {
			showError(jqXHR.responseJSON.message);
		}
	});
}

function renderComments(comments) {
	const list = $('#comments-list');
	list.empty();

	if (comments.length === 0) {
		list.append($('<li>').addClass('list-group-item').text('No comments yet'));
		return;
	}

	comments.forEach(function (comment) {
		const item = $('<li>').addClass('list-group-item');

		const header = $('<div>').addClass('d-flex justify-content-between');
		header.append($('<strong>').text(comment.authorName));
		header.append($('<small>').addClass('text-muted').text(comment.createdAt));
		item.append(header);

		item.append($('<p>').addClass('mb-0 mt-1').text(comment.content));

		list.append(item);
	});
}

function submitComment() {
	const content = $('#comment-content').val();
	if (!content) {
		return;
	}

	$.ajax({
		url: '/api/submission-comments',
		method: 'POST',
		contentType: 'application/json',
		data: JSON.stringify({submissionId: submissionId, content: content})
	})
		.done(function () {
			$('#comment-content').val('');
			loadComments();
		})
		.fail(function (jqXHR) {
			showError(jqXHR.responseJSON.message);
		});
}
