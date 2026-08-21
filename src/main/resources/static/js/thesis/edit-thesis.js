$(function () {
	loadThesis();

	$('#save-thesis-btn').on('click', function (event) {
		event.preventDefault();

		const title = $('#title').val();
		const abstractText = $('#abstractText').val();

		const errors = [];
		validateRequired(title, 'Title', errors);
		validateMaxLength(title, 255, 'Title', errors);

		if (errors.length > 0) {
			showValidationErrors(errors);
			return;
		}

		const payload = {
			title: title,
			abstractText: abstractText
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
				showError(jqXHR.responseJSON.message);
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
			showError(jqXHR.responseJSON.message);
		}
	});
}
