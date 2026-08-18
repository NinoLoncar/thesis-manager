$(function () {
	fetchSubmissions();
});

function fetchSubmissions() {
	$.ajax({
		url: '/api/submissions',
		method: 'GET',
		data: { thesisId: thesisId },
		success: function (submissions) {
			renderSubmissions(submissions);
		},
		error: function (jqXHR) {
			showError(jqXHR.responseJSON.message);
		}
	});
}

function renderSubmissions(submissions) {
	const list = $('#submissions-list');
	list.empty();

	if (submissions.length === 0) {
		list.append($('<li>').addClass('list-group-item').text('No submissions yet'));
		return;
	}

	submissions.forEach(function (submission) {
		const item = $('<li>').addClass('list-group-item');

		const header = $('<div>').addClass('d-flex justify-content-between align-items-center');
		header.append($('<strong>').text('Version ' + submission.version + ' - ' + submission.fileName));
		header.append($('<span>').addClass('badge bg-secondary').text(submission.status));
		item.append(header);

		if (submission.description) {
			item.append($('<p>').addClass('mb-1 mt-2').text(submission.description));
		}

		const info = $('<small>').addClass('text-muted');
		info.text('Submitted by ' + submission.studentName + ' on ' + submission.createdAt);
		item.append($('<div>').append(info));

		if (submission.reviewedByName) {
			const reviewInfo = $('<small>').addClass('text-muted');
			reviewInfo.text('Reviewed by ' + submission.reviewedByName + ' on ' + submission.reviewedAt);
			item.append($('<div>').append(reviewInfo));
		}

		const detailsBtn = $('<button>').text('Details').addClass('btn btn-sm btn-secondary mt-2').on('click', function () {
			window.location.href = '/theses/' + thesisId + '/submissions/' + submission.id;
		});
		item.append(detailsBtn);

		list.append(item);
	});
}
