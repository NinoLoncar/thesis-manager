function showError(message) {
	const notification = $('<div>', {
		class: 'toast align-items-center text-bg-danger border-0',
		role: 'alert'
	}).html(
		'<div class="d-flex">' +
			'<div class="toast-body"></div>' +
			'<button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>' +
			'</div>'
	);

	notification.find('.toast-body').text(message);
	$('#toast-container').append(notification);

	const bsToast = new bootstrap.Toast(notification[0], {delay: 5000});
	bsToast.show();

	notification.on('hidden.bs.toast', function () {
		notification.remove();
	});
}
