$(function () {
	loadRoles();

	$('#create-user-btn').on('click', function (event) {
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
			url: '/api/users',
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(payload)
		})
			.done(function () {
				window.location.href = '/users';
			})
			.fail(function (jqXHR) {
				console.error('Failed to create user: ' + jqXHR.status);
			});
	});
});

function loadRoles() {
	$.ajax({
		url: '/api/roles',
		method: 'GET',
		success: function (roles) {
			renderRoles(roles);
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
		const wrapper = $('<div>').addClass('form-check');
		const checkbox = $('<input>', {
			type: 'checkbox',
			class: 'role-checkbox form-check-input',
			id: 'role-' + role.id,
			value: role.id
		});
		const label = $('<label>', {
			class: 'form-check-label',
			for: 'role-' + role.id,
			text: role.name
		});

		wrapper.append(checkbox).append(label);
		container.append(wrapper);
	});
}
