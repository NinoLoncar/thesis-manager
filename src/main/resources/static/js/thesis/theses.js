$(function () {
	fetchTheses();

	$('#filter-btn').on('click', function (event) {
		event.preventDefault();
		fetchTheses();
	});

	$('#clear-filter-btn').on('click', function (event) {
		event.preventDefault();

		$('#filter-title').val('');
		$('#filter-mentor-name').val('');
		$('#filter-reserved').val('');

		fetchTheses();
	});
});

function fetchTheses() {
	$.ajax({
		url: '/api/theses',
		method: 'GET',
		data: {
			title: $('#filter-title').val(),
			mentorName: $('#filter-mentor-name').val(),
			reserved: $('#filter-reserved').val()
		},
		success: function (theses) {
			renderTheses(theses);
		},
		error: function (jqXHR) {
			showError(jqXHR.responseJSON.message);
		}
	});
}

function renderTheses(theses) {
	const list = $('#theses-list');
	list.empty();

	theses.forEach(function (thesis) {
		const item = $('<li>')
			.addClass('list-group-item d-flex justify-content-between align-items-center')
			.text(thesis.title + ' (' + thesis.mentorName + ')');

		const detailsBtn = $('<button>').text('Details').addClass('btn btn-sm btn-secondary').on('click', function () {
			window.location.href = '/theses/' + thesis.id;
		});

		item.append(detailsBtn);
		list.append(item);
	});
}
