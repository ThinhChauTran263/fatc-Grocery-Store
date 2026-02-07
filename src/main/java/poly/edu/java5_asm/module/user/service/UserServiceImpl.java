package poly.edu.java5_asm.module.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.java5_asm.common.exception.AuthException;
import poly.edu.java5_asm.common.exception.UserNotFoundException;
import poly.edu.java5_asm.module.user.dto.request.ProfileUpdateRequest;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.module.user.repository.UserRepository;

/**
 * Implementation của UserService
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Không tìm thấy người dùng: " + username));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    @Transactional
    public User updateProfile(Long userId, ProfileUpdateRequest request) {
        User user = findById(userId);

        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw AuthException.emailExists();
        }

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        // Xử lý đổi tên đăng nhập
        if (request.getNewUsername() != null && !request.getNewUsername().isBlank()) {
            // Kiểm tra username mới không trùng với username hiện tại
            if (!user.getUsername().equals(request.getNewUsername())) {
                // Kiểm tra username mới đã tồn tại chưa
                if (userRepository.existsByUsername(request.getNewUsername())) {
                    throw new AuthException("Tên đăng nhập đã tồn tại");
                }
                user.setUsername(request.getNewUsername());
            }
        }

        // Xử lý đổi mật khẩu với bảo mật tăng cường
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            // Kiểm tra mật khẩu hiện tại phải được nhập
            if (request.getCurrentPassword() == null || request.getCurrentPassword().isBlank()) {
                throw new AuthException("Vui lòng nhập mật khẩu hiện tại để đổi mật khẩu");
            }
            
            // Xác thực mật khẩu hiện tại
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new AuthException("Mật khẩu hiện tại không chính xác");
            }
            
            // Kiểm tra độ dài mật khẩu mới
            if (request.getNewPassword().length() < 6 || request.getNewPassword().length() > 100) {
                throw AuthException.invalidPassword();
            }
            
            // Kiểm tra mật khẩu mới không trùng với mật khẩu cũ
            if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
                throw new AuthException("Mật khẩu mới không được trùng với mật khẩu hiện tại");
            }
            
            // Kiểm tra xác nhận mật khẩu
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                throw AuthException.passwordMismatch();
            }
            
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        return userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public User updateAvatar(Long userId, String avatarUrl) {
        User user = findById(userId);
        user.setAvatarUrl(avatarUrl);
        return userRepository.saveAndFlush(user);
    }
}
