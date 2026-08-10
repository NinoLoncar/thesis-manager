$(function () {
	$('#login-btn').on('click', function (event) {
		event.preventDefault();

		const email = $('#email').val();
		const password = $('#password').val();

		$.ajax({
			url: '/login',
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({
				email: email,
				password: password
			})
		})
			.done(function (data) {
				window.location.href = '/';
			})
			.fail(function (jqXHR) {
				console.error('Login failed: ' + jqXHR.status);
			});
	});
});
