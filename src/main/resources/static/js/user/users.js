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
		let editBtn = $('<button>').text('Edit');
		let deleteBtn = $('<button>').text('Delete');

		item.append(' ').append(editBtn).append(' ').append(deleteBtn);
		list.append(item);
	});
}
