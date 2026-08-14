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
			console.log('Failed to load users: ' + jqXHR.status);
		}
	});
}

function renderUsers(users) {
	let list = $('#users-list');
	list.empty();

	users.forEach(function (user) {
		let item = $('<li>').text(user.email);
		let editBtn = $('<button>').text('Edit').on('click', function () {
			window.location.href = '/users/' + user.id + '/edit';
		});
		let deleteBtn = $('<button>').text('Delete').on('click', function () {
			deleteUser(user.id);
		});

		item.append(' ').append(editBtn).append(' ').append(deleteBtn);
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
			console.log('Failed to delete user ' + jqXHR.status);
		}
	});
}
