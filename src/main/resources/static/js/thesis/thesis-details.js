$(function () {
	loadThesis();

	if (canManageReservations) {
		loadReservations();
	}

	$('#reserve-btn').on('click', function () {
		$.ajax({
			url: '/api/reservations',
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({ thesisId: thesisId })
		})
			.done(function () {
				loadThesis();
			})
			.fail(function (jqXHR) {
				console.error('Failed to reserve thesis: ' + jqXHR.status);
			});
	});
});

function loadThesis() {
	$.ajax({
		url: '/api/theses/' + thesisId,
		method: 'GET',
		success: function (thesis) {
			renderThesis(thesis);
		},
		error: function (jqXHR) {
			console.error('Failed to load thesis: ' + jqXHR.status);
		}
	});
}

function renderThesis(thesis) {
	$('#thesis-title').text(thesis.title);
	$('#thesis-abstract').text(thesis.abstractText);
	$('#thesis-type').text(thesis.type);
	$('#thesis-status').text(thesis.status);
	$('#thesis-mentor').text(thesis.mentorName);
	$('#thesis-student').text(thesis.studentName || 'Not reserved');

	if (canCreateReservation && !thesis.studentId) {
		$('#reserve-btn').removeClass('d-none');
	} else {
		$('#reserve-btn').addClass('d-none');
	}
}

function loadReservations() {
	$.ajax({
		url: '/api/reservations',
		method: 'GET',
		data: { thesisId: thesisId },
		success: function (reservations) {
			renderReservations(reservations);
		},
		error: function (jqXHR) {
			console.error('Failed to load reservations: ' + jqXHR.status);
		}
	});
}

function renderReservations(reservations) {
	const list = $('#reservations-list');
	list.empty();

	reservations.forEach(function (reservation) {
		const item = $('<li>')
			.addClass('list-group-item d-flex justify-content-between align-items-center')
			.text(reservation.studentName + ' - ' + reservation.status);

		if (reservation.status === 'PENDING') {
			const approveBtn = $('<button>').text('Approve').addClass('btn btn-sm btn-success me-2').on('click', function () {
				updateReservation(reservation.id, 'approve');
			});
			const denyBtn = $('<button>').text('Deny').addClass('btn btn-sm btn-danger').on('click', function () {
				updateReservation(reservation.id, 'deny');
			});

			item.append($('<div>').append(approveBtn).append(denyBtn));
		}

		list.append(item);
	});
}

function updateReservation(id, action) {
	$.ajax({
		url: '/api/reservations/' + id + '/' + action,
		method: 'PUT'
	})
		.done(function () {
			loadReservations();
			loadThesis();
		})
		.fail(function (jqXHR) {
			console.error('Failed to update reservation: ' + jqXHR.status);
			alert(jqXHR.responseText || 'Failed to update reservation');
		});
}
