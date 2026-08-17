$(function () {
	$('#submit-btn').on('click', function (event) {
		event.preventDefault();

		const fileInput = $('#file')[0];
		if (fileInput.files.length === 0) {
			showError('Please choose a file');
			return;
		}

		const formData = new FormData();
		formData.append('thesisId', thesisId);
		formData.append('description', $('#description').val());
		formData.append('file', fileInput.files[0]);

		$.ajax({
			url: '/api/submissions',
			method: 'POST',
			data: formData,
			processData: false,
			contentType: false
		})
			.done(function () {
				window.location.href = '/theses/' + thesisId + '/submissions';
			})
			.fail(function (jqXHR) {
				showError(jqXHR.responseJSON.message);
			});
	});
});
