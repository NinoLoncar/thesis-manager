$(function () {
	loadThesis();

	$('#save-thesis-btn').on('click', function (event) {
		event.preventDefault();

		const payload = {
			title: $('#title').val(),
			abstractText: $('#abstractText').val()
		};

		$.ajax({
			url: '/api/theses/' + thesisId,
			method: 'PUT',
			contentType: 'application/json',
			data: JSON.stringify(payload)
		})
			.done(function () {
				window.location.href = '/theses/mentored';
			})
			.fail(function (jqXHR) {
				console.error('Failed to update thesis: ' + jqXHR.status);
			});
	});
});

function loadThesis() {
	$.ajax({
		url: '/api/theses/' + thesisId,
		method: 'GET',
		success: function (thesis) {
			$('#title').val(thesis.title);
			$('#abstractText').val(thesis.abstractText);
		},
		error: function (jqXHR) {
			console.error('Failed to load thesis: ' + jqXHR.status);
		}
	});
}
