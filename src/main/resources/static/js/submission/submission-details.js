$(function () {
	$.ajax({
		url: '/api/submissions/' + submissionId,
		method: 'GET',
		success: function (submission) {
			renderSubmission(submission);
		},
		error: function (jqXHR) {
			showError(jqXHR.responseJSON.message);
		}
	});
});

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
