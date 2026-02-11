/**
 * Profile Form Validation
 * Validates user profile update form with real-time feedback
 */

(function() {
    'use strict';

    // Validation rules matching backend constraints
    const ValidationRules = {
        fullName: {
            required: true,
            minLength: 2,
            maxLength: 100,
            pattern: /^[\p{L}\s]+$/u, // Unicode letters and spaces
            messages: {
                required: 'Họ tên không được để trống',
                minLength: 'Họ tên phải có ít nhất 2 ký tự',
                maxLength: 'Họ tên không được vượt quá 100 ký tự',
                pattern: 'Họ tên chỉ được chứa chữ cái và khoảng trắng'
            }
        },
        email: {
            required: true,
            pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
            messages: {
                required: 'Email không được để trống',
                pattern: 'Email không hợp lệ (ví dụ: example@domain.com)'
            }
        },
        phone: {
            required: false,
            pattern: /^[0-9\s\-\+\(\)]{0,20}$/,
            messages: {
                pattern: 'Số điện thoại không hợp lệ (chỉ chứa số, dấu cách, +, -, (, ))'
            }
        },
        newUsername: {
            required: false,
            minLength: 3,
            maxLength: 50,
            pattern: /^[a-zA-Z0-9_]{3,50}$/,
            messages: {
                minLength: 'Tên đăng nhập phải có ít nhất 3 ký tự',
                maxLength: 'Tên đăng nhập không được vượt quá 50 ký tự',
                pattern: 'Tên đăng nhập chỉ được chứa chữ cái, số và dấu gạch dưới (_)'
            }
        },
        newPassword: {
            required: false,
            minLength: 6,
            messages: {
                minLength: 'Mật khẩu mới phải có ít nhất 6 ký tự',
                requiresCurrent: 'Vui lòng nhập mật khẩu hiện tại để xác thực'
            }
        },
        confirmPassword: {
            required: false,
            mustMatch: 'newPassword',
            messages: {
                mustMatch: 'Mật khẩu xác nhận không khớp với mật khẩu mới'
            }
        },
        currentPassword: {
            required: false,
            messages: {
                requiredForChange: 'Mật khẩu hiện tại là bắt buộc khi thay đổi mật khẩu'
            }
        }
    };

    // Validate single field
    function validateField(fieldName, value, allValues = {}) {
        const rules = ValidationRules[fieldName];
        if (!rules) return { valid: true };

        const errors = [];

        // Required check
        if (rules.required && (!value || value.trim() === '')) {
            return { valid: false, message: rules.messages.required };
        }

        // Skip other validations if field is empty and not required
        if (!rules.required && (!value || value.trim() === '')) {
            return { valid: true };
        }

        const trimmedValue = value.trim();

        // Min length check
        if (rules.minLength && trimmedValue.length < rules.minLength) {
            return { valid: false, message: rules.messages.minLength };
        }

        // Max length check
        if (rules.maxLength && trimmedValue.length > rules.maxLength) {
            return { valid: false, message: rules.messages.maxLength };
        }

        // Pattern check
        if (rules.pattern && !rules.pattern.test(trimmedValue)) {
            return { valid: false, message: rules.messages.pattern };
        }

        // Must match check (for confirm password)
        if (rules.mustMatch) {
            const matchValue = allValues[rules.mustMatch];
            if (matchValue && trimmedValue !== matchValue) {
                return { valid: false, message: rules.messages.mustMatch };
            }
        }

        return { valid: true };
    }

    // Show error message
    function showError(inputElement, message) {
        const wrapper = inputElement.closest('.edit-form__input-wrap');
        const group = inputElement.closest('.edit-form__group');
        
        if (wrapper) {
            wrapper.classList.add('edit-form__input-wrap--error');
        }

        // Remove existing error message
        const existingError = group.querySelector('.edit-form__error');
        if (existingError) {
            existingError.remove();
        }

        // Add new error message
        if (message) {
            const errorElement = document.createElement('p');
            errorElement.className = 'edit-form__error';
            errorElement.textContent = message;
            wrapper.parentNode.insertBefore(errorElement, wrapper.nextSibling);
        }
    }

    // Clear error message
    function clearError(inputElement) {
        const wrapper = inputElement.closest('.edit-form__input-wrap');
        const group = inputElement.closest('.edit-form__group');
        
        if (wrapper) {
            wrapper.classList.remove('edit-form__input-wrap--error');
        }

        const existingError = group.querySelector('.edit-form__error');
        if (existingError && !existingError.hasAttribute('th:if')) {
            existingError.remove();
        }
    }

    // Get all form values
    function getFormValues(form) {
        return {
            fullName: form.querySelector('#full-name')?.value || '',
            email: form.querySelector('#email-address')?.value || '',
            phone: form.querySelector('#phone-number')?.value || '',
            newUsername: form.querySelector('#new-username')?.value || '',
            currentPassword: form.querySelector('#current-password')?.value || '',
            newPassword: form.querySelector('#new-password')?.value || '',
            confirmPassword: form.querySelector('#confirm-password')?.value || ''
        };
    }

    // Initialize validation
    function initValidation() {
        const form = document.querySelector('.edit-form');
        if (!form) return;

        const fields = {
            fullName: form.querySelector('#full-name'),
            email: form.querySelector('#email-address'),
            phone: form.querySelector('#phone-number'),
            newUsername: form.querySelector('#new-username'),
            currentPassword: form.querySelector('#current-password'),
            newPassword: form.querySelector('#new-password'),
            confirmPassword: form.querySelector('#confirm-password')
        };

        // Real-time validation on blur
        Object.keys(fields).forEach(fieldName => {
            const field = fields[fieldName];
            if (!field) return;

            field.addEventListener('blur', function() {
                const allValues = getFormValues(form);
                const result = validateField(fieldName, this.value, allValues);
                
                if (!result.valid) {
                    showError(this, result.message);
                } else {
                    clearError(this);
                }
            });

            // Clear error on input
            field.addEventListener('input', function() {
                clearError(this);
            });
        });

        // Special handling for password fields
        if (fields.newPassword && fields.currentPassword) {
            fields.newPassword.addEventListener('input', function() {
                const wrapper = fields.currentPassword.closest('.edit-form__input-wrap');
                if (this.value.length > 0 && !fields.currentPassword.value) {
                    wrapper.style.borderColor = '#ffc107';
                    wrapper.style.boxShadow = '0 0 0 4px rgba(255, 193, 7, 0.15)';
                } else {
                    wrapper.style.borderColor = '';
                    wrapper.style.boxShadow = '';
                }
            });

            fields.currentPassword.addEventListener('input', function() {
                if (this.value.length > 0) {
                    const wrapper = this.closest('.edit-form__input-wrap');
                    wrapper.style.borderColor = '';
                    wrapper.style.boxShadow = '';
                }
            });
        }

        // Validate confirm password when new password changes
        if (fields.newPassword && fields.confirmPassword) {
            fields.newPassword.addEventListener('input', function() {
                if (fields.confirmPassword.value) {
                    const allValues = getFormValues(form);
                    const result = validateField('confirmPassword', fields.confirmPassword.value, allValues);
                    if (!result.valid) {
                        showError(fields.confirmPassword, result.message);
                    } else {
                        clearError(fields.confirmPassword);
                    }
                }
            });
        }

        // Form submission validation
        form.addEventListener('submit', function(e) {
            const allValues = getFormValues(form);
            let hasErrors = false;
            const errors = [];

            // Validate all required fields
            ['fullName', 'email'].forEach(fieldName => {
                const field = fields[fieldName];
                if (!field) return;

                const result = validateField(fieldName, field.value, allValues);
                if (!result.valid) {
                    showError(field, result.message);
                    errors.push(result.message);
                    hasErrors = true;
                }
            });

            // Validate optional fields if they have values
            ['phone', 'newUsername'].forEach(fieldName => {
                const field = fields[fieldName];
                if (!field || !field.value.trim()) return;

                const result = validateField(fieldName, field.value, allValues);
                if (!result.valid) {
                    showError(field, result.message);
                    errors.push(result.message);
                    hasErrors = true;
                }
            });

            // Password change validation
            const hasNewPassword = allValues.newPassword.trim().length > 0;
            const hasCurrentPassword = allValues.currentPassword.trim().length > 0;
            const hasConfirmPassword = allValues.confirmPassword.trim().length > 0;

            if (hasNewPassword) {
                // Check if current password is provided
                if (!hasCurrentPassword) {
                    showError(fields.currentPassword, ValidationRules.currentPassword.messages.requiredForChange);
                    errors.push(ValidationRules.currentPassword.messages.requiredForChange);
                    hasErrors = true;
                }

                // Validate new password
                const newPasswordResult = validateField('newPassword', allValues.newPassword, allValues);
                if (!newPasswordResult.valid) {
                    showError(fields.newPassword, newPasswordResult.message);
                    errors.push(newPasswordResult.message);
                    hasErrors = true;
                }

                // Validate confirm password
                if (hasConfirmPassword) {
                    const confirmResult = validateField('confirmPassword', allValues.confirmPassword, allValues);
                    if (!confirmResult.valid) {
                        showError(fields.confirmPassword, confirmResult.message);
                        errors.push(confirmResult.message);
                        hasErrors = true;
                    }
                } else {
                    showError(fields.confirmPassword, 'Vui lòng xác nhận mật khẩu mới');
                    errors.push('Vui lòng xác nhận mật khẩu mới');
                    hasErrors = true;
                }
            }

            if (hasErrors) {
                e.preventDefault();
                
                // Show notification if available
                if (typeof NotificationModal !== 'undefined' && NotificationModal.error) {
                    NotificationModal.error(
                        'Vui lòng kiểm tra lại thông tin đã nhập:\n• ' + errors.join('\n• '),
                        'Lỗi xác thực'
                    );
                }

                // Focus on first error field
                const firstErrorField = form.querySelector('.edit-form__input-wrap--error input');
                if (firstErrorField) {
                    firstErrorField.focus();
                }

                return false;
            }
        });
    }

    // Initialize when DOM is ready
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initValidation);
    } else {
        initValidation();
    }
})();
