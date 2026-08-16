$(function () {
	fetchUsers();
});

function fetchUsers() {
	$.ajax({
		url: '/api/users',
		method: 'GET',
		success: function (users) {
			renderUsers(users);
		},
		error: function (jqXHR) {
			showError(jqXHR.responseJSON.message);
		}
	});
}

function renderUsers(users) {
	let list = $('#users-list');
	list.empty();

	users.forEach(function (user) {
		let item = $('<li>')
			.addClass('list-group-item d-flex justify-content-between align-items-center')
			.text(user.email);

		let editBtn = $('<button>').text('Edit').addClass('btn btn-sm btn-primary me-2').on('click', function () {
			window.location.href = '/users/' + user.id + '/edit';
		});
		let deleteBtn = $('<button>').text('Delete').addClass('btn btn-sm btn-danger').on('click', function () {
			deleteUser(user.id);
		});

		let actions = $('<div>').append(editBtn).append(deleteBtn);
		item.append(actions);
		list.append(item);
	});
}

function deleteUser(userId) {
	$.ajax({
		url: '/api/users/' + userId,
		method: 'DELETE',
		success: function (users) {
			fetchUsers();
		},
		error: function (jqXHR) {
			showError(jqXHR.responseJSON.message);
		}
	});
}
