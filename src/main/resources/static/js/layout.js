$(function () {
	$('#logout-btn').on('click', function (event) {
		event.preventDefault();

		$.ajax({
			url: '/logout',
			method: 'POST'
		})
			.done(function () {
				window.location.href = '/login';
			})
			.fail(function (jqXHR) {
				console.error('Logout failed: ' + jqXHR.status);
			});
	});
});
