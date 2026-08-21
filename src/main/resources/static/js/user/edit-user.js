$(function () {
	loadRoles(function () {
		loadUser();
	});

	$('#save-user-btn').on('click', function (event) {
		event.preventDefault();

		const email = $('#email').val();
		const firstName = $('#firstName').val();
		const lastName = $('#lastName').val();
		const password = $('#password').val();

		const errors = [];
		validateRequired(email, 'Email', errors);
		validateEmail(email, 'Email', errors);
		validateRequired(firstName, 'First name', errors);
		validateRequired(lastName, 'Last name', errors);
		validateMinLength(password, 6, 'Password', errors);

		if (errors.length > 0) {
			showValidationErrors(errors);
			return;
		}

		const roleIds = $('.role-checkbox:checked').map(function () {
			return parseInt($(this).val(), 10);
		}).get();

		const payload = {
			email: email,
			firstName: firstName,
			lastName: lastName,
			password: password,
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
				showError(jqXHR.responseJSON.message);
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
			showError(jqXHR.responseJSON.message);
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
			showError(jqXHR.responseJSON.message);
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

function checkAssignedRoles(roleIds) {
	roleIds.forEach(function (roleId) {
		$('.role-checkbox[value="' + roleId + '"]').prop('checked', true);
	});
}
