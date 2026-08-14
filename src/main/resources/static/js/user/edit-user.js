$(function () {
	loadRoles(function () {
		loadUser();
	});

	$('#save-user-btn').on('click', function (event) {
		event.preventDefault();

		const roleIds = $('.role-checkbox:checked').map(function () {
			return parseInt($(this).val(), 10);
		}).get();

		const payload = {
			email: $('#email').val(),
			firstName: $('#firstName').val(),
			lastName: $('#lastName').val(),
			password: $('#password').val(),
			roleIds: roleIds
		};

		$.ajax({
			url: '/api/users/' + userId,
			method: 'PUT',
			contentType: 'application/json',
			data: JSON.stringify(payload)
		})
			.done(function () {
				window.location.href = '/users';
			})
			.fail(function (jqXHR) {
				console.error('Failed to update user: ' + jqXHR.status);
			});
	});
});

function loadUser() {
	$.ajax({
		url: '/api/users/' + userId,
		method: 'GET',
		success: function (user) {
			$('#email').val(user.email);
			$('#firstName').val(user.firstName);
			$('#lastName').val(user.lastName);
			checkAssignedRoles(user.roleIds);
		},
		error: function (jqXHR) {
			console.error('Failed to load user: ' + jqXHR.status);
		}
	});
}

function loadRoles(onComplete) {
	$.ajax({
		url: '/api/roles',
		method: 'GET',
		success: function (roles) {
			renderRoles(roles);
			if (onComplete) {
				onComplete();
			}
		},
		error: function (jqXHR) {
			console.error('Failed to load roles: ' + jqXHR.status);
		}
	});
}

function renderRoles(roles) {
	const container = $('#roles-list');
	container.empty();

	roles.forEach(function (role) {
		const label = $('<label>');
		const checkbox = $('<input>', {
			type: 'checkbox',
			class: 'role-checkbox',
			value: role.id
		});

		label.append(checkbox).append(' ' + role.name);
		container.append(label).append('<br>');
	});
}

function checkAssignedRoles(roleIds) {
	roleIds.forEach(function (roleId) {
		$('.role-checkbox[value="' + roleId + '"]').prop('checked', true);
	});
}
