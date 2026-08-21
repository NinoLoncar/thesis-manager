$(function () {
	$('#create-thesis-btn').on('click', function (event) {
		event.preventDefault();

		const title = $('#title').val();
		const abstractText = $('#abstractText').val();
		const type = $('#type').val();

		const errors = [];
		validateRequired(title, 'Title', errors);
		validateMaxLength(title, 255, 'Title', errors);
		validateRequired(type, 'Type', errors);

		if (errors.length > 0) {
			showValidationErrors(errors);
			return;
		}

		const payload = {
			title: title,
			abstractText: abstractText,
			type: type
		};

		$.ajax({
			url: '/api/theses',
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(payload)
		})
			.done(function () {
				window.location.href = '/';
			})
			.fail(function (jqXHR) {
				showError(jqXHR.responseJSON.message);
			});
	});
});
