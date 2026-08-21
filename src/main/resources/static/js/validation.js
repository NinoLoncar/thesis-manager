function isBlank(value) {
	return value === null || value === undefined || value.toString().trim().length === 0;
}

function isValidEmail(value) {
	return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
}

function validateRequired(value, fieldLabel, errors) {
	if (isBlank(value)) {
		errors.push(fieldLabel + ' is required');
	}
}

function validateEmail(value, fieldLabel, errors) {
	if (!isBlank(value) && !isValidEmail(value)) {
		errors.push(fieldLabel + ' is not a valid email');
	}
}

function validateMinLength(value, minLength, fieldLabel, errors) {
	if (!isBlank(value) && value.trim().length < minLength) {
		errors.push(fieldLabel + ' must be at least ' + minLength + ' characters');
	}
}

function validateMaxLength(value, maxLength, fieldLabel, errors) {
	if (value && value.length > maxLength) {
		errors.push(fieldLabel + ' must be at most ' + maxLength + ' characters');
	}
}

function showValidationErrors(errors) {
	errors.forEach(function (message) {
		showError(message);
	});
}
