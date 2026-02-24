package poly.edu.java5_asm.DevThai;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import poly.edu.java5_asm.common.exception.AuthException;
import poly.edu.java5_asm.module.auth.dto.request.RegisterRequest;
import poly.edu.java5_asm.module.auth.dto.request.ResetPasswordRequest;
import poly.edu.java5_asm.module.auth.service.AuthServiceImpl;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.module.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test cases cho AuthService - Quốc Thái
 * Test cases: AUTH_001 đến AUTH_007
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;

    @Before
    public void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .fullName("Test User")
                .password("encodedPassword")
                .role(User.Role.USER)
                .isActive(true)
                .loginCount(0)
                .build();
    }

    /**
     * AUTH_001: testRegister_Success
     * Đăng ký tài khoản mới thành công
     */
    @Test
    public void testRegister_Success() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("new@example.com");
        request.setPassword("Pass123!");
        request.setConfirmPassword("Pass123!");
        request.setFullName("New User");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = authService.register(request);

        // Then
        assertNotNull("User should be created", result);
        verify(userRepository, times(1)).existsByUsername("newuser");
        verify(userRepository, times(1)).existsByEmail("new@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * AUTH_002: testRegister_DuplicateEmail
     * Đăng ký với email đã tồn tại
     */
    @Test(expected = AuthException.class)
    public void testRegister_DuplicateEmail() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("existing@example.com");
        request.setPassword("Pass123!");
        request.setConfirmPassword("Pass123!");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // When
        authService.register(request);

        // Then - expect AuthException
    }

    /**
     * AUTH_003: testRegister_DuplicateUsername
     * Đăng ký với username đã tồn tại
     */
    @Test(expected = AuthException.class)
    public void testRegister_DuplicateUsername() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existinguser");
        request.setEmail("new@example.com");
        request.setPassword("Pass123!");
        request.setConfirmPassword("Pass123!");

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        // When
        authService.register(request);

        // Then - expect AuthException
    }

    /**
     * AUTH_004: testRegister_WeakPassword
     * Đăng ký với mật khẩu yếu (password mismatch)
     */
    @Test(expected = AuthException.class)
    public void testRegister_WeakPassword() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("new@example.com");
        request.setPassword("123");
        request.setConfirmPassword("456"); // Mismatch to trigger exception

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);

        // When
        authService.register(request);

        // Then - expect AuthException
    }

    /**
     * AUTH_005: testUpdateLastLogin_Success
     * Cập nhật thời gian đăng nhập cuối
     */
    @Test
    public void testUpdateLastLogin_Success() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        authService.updateLastLogin("testuser");

        // Then
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(userRepository, times(1)).save(any(User.class));
        assertNotNull("Last login should be updated", testUser.getLastLoginAt());
        assertEquals("Login count should be incremented", 1L, (long) testUser.getLoginCount());
    }

    /**
     * AUTH_006: testResetPassword_Success
     * Đặt lại mật khẩu thành công
     */
    @Test
    public void testResetPassword_Success() {
        // Given
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setUsernameOrEmail("testuser");
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("NewPass123!");
        request.setConfirmPassword("NewPass123!");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("oldPassword", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.matches("NewPass123!", "encodedPassword")).thenReturn(false);
        when(passwordEncoder.encode("NewPass123!")).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        authService.resetPassword(request);

        // Then
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(passwordEncoder, times(1)).matches("oldPassword", "encodedPassword");
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * AUTH_007: testResetPassword_InvalidToken
     * Đặt lại mật khẩu với token không hợp lệ (current password incorrect)
     */
    @Test(expected = AuthException.class)
    public void testResetPassword_InvalidToken() {
        // Given
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setUsernameOrEmail("testuser");
        request.setCurrentPassword("wrongPassword");
        request.setNewPassword("NewPass123!");
        request.setConfirmPassword("NewPass123!");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // When
        authService.resetPassword(request);

        // Then - expect AuthException
    }
}
