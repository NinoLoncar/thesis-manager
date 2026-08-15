$(function () {
	fetchTheses();
});

function fetchTheses() {
	$.ajax({
		url: '/api/theses',
		method: 'GET',
		data: { mentorId: mentorId },
		success: function (theses) {
			renderTheses(theses);
		},
		error: function (jqXHR) {
			console.error('Failed to load theses: ' + jqXHR.status);
		}
	});
}

function renderTheses(theses) {
	const list = $('#theses-list');
	list.empty();

	theses.forEach(function (thesis) {
		const item = $('<li>')
			.addClass('list-group-item d-flex justify-content-between align-items-center')
			.text(thesis.title);

		const detailsBtn = $('<button>').text('Details').addClass('btn btn-sm btn-secondary me-2');
		const editBtn = $('<button>').text('Edit').addClass('btn btn-sm btn-primary me-2');
		const deleteBtn = $('<button>').text('Delete').addClass('btn btn-sm btn-danger').on('click', function () {
			deleteThesis(thesis.id);
		});

		const actions = $('<div>').append(detailsBtn).append(editBtn).append(deleteBtn);
		item.append(actions);
		list.append(item);
	});
}

function deleteThesis(thesisId) {
	$.ajax({
		url: '/api/theses/' + thesisId,
		method: 'DELETE',
		success: function () {
			fetchTheses();
		},
		error: function (jqXHR) {
			console.error('Failed to delete thesis: ' + jqXHR.status);
		}
	});
}
