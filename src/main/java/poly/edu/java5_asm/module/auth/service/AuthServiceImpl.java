package poly.edu.java5_asm.module.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.java5_asm.common.exception.AuthException;
import poly.edu.java5_asm.module.auth.dto.request.RegisterRequest;
import poly.edu.java5_asm.module.auth.dto.request.ResetPasswordRequest;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.module.user.repository.UserRepository;

import java.time.LocalDateTime;

/**
 * Implementation của AuthService
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw AuthException.usernameExists();
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw AuthException.emailExists();
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw AuthException.passwordMismatch();
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(User.Role.USER)
                .isActive(true)
                .build();

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateLastLogin(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setLastLoginAt(LocalDateTime.now());
            user.setLoginCount(user.getLoginCount() + 1);
            userRepository.save(user);
        });
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        // Kiểm tra mật khẩu mới và xác nhận mật khẩu
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw AuthException.passwordMismatch();
        }

        // Tìm user theo username hoặc email
        User user = userRepository.findByUsername(request.getUsernameOrEmail())
                .or(() -> userRepository.findByEmail(request.getUsernameOrEmail()))
                .orElseThrow(() -> new AuthException("Không tìm thấy tài khoản với thông tin này"));

        // Kiểm tra nếu là OAuth2 user (không có password)
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new AuthException("Tài khoản đăng nhập bằng Google không thể đổi mật khẩu");
        }

        // Xác thực mật khẩu hiện tại
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new AuthException("Mật khẩu hiện tại không chính xác");
        }

        // Kiểm tra mật khẩu mới không trùng với mật khẩu cũ
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new AuthException("Mật khẩu mới không được trùng với mật khẩu hiện tại");
        }

        // Cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
