$(function () {
	if (canViewTheses) {
		loadMyThesis();
	}
});

function loadMyThesis() {
	$.ajax({
		url: '/api/theses',
		method: 'GET',
		data: {studentId: currentUserId},
		success: function (theses) {
			if (theses.length > 0) {
				renderMyThesis(theses[0]);
			}
		},
		error: function (jqXHR) {
			showError(jqXHR.responseJSON.message);
		}
	});
}

function renderMyThesis(thesis) {
	$('#my-thesis-title').text(thesis.title);
	$('#my-thesis-status').text(thesis.status);
	$('#my-thesis-mentor').text(thesis.mentorName);
	$('#my-thesis-link').attr('href', '/theses/' + thesis.id);
	$('#my-thesis-section').removeClass('d-none');
}
