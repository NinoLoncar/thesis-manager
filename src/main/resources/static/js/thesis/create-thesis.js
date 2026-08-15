$(function () {
	$('#create-thesis-btn').on('click', function (event) {
		event.preventDefault();

		const payload = {
			title: $('#title').val(),
			abstractText: $('#abstractText').val(),
			type: $('#type').val()
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
				console.error('Failed to create thesis: ' + jqXHR.status);
			});
	});
});
