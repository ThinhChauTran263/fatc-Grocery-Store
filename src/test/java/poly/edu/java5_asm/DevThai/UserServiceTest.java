package poly.edu.java5_asm.DevThai;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import poly.edu.java5_asm.common.exception.UserNotFoundException;
import poly.edu.java5_asm.module.user.dto.request.ProfileUpdateRequest;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.module.user.repository.UserRepository;
import poly.edu.java5_asm.module.user.service.UserServiceImpl;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test cases cho UserService - Quốc Thái
 * Test cases: US_001 đến US_006
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

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
                .build();
    }

    /**
     * US_001: testFindByUsername_Success
     * Tìm user theo username hợp lệ
     */
    @Test
    public void testFindByUsername_Success() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        User result = userService.findByUsername("testuser");

        // Then
        assertNotNull("User object should not be null", result);
        assertEquals("Username should match", "testuser", result.getUsername());
        assertEquals("Email should match", "test@example.com", result.getEmail());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    /**
     * US_002: testFindByUsername_NotFound
     * Tìm user với username không tồn tại
     */
    @Test(expected = UserNotFoundException.class)
    public void testFindByUsername_NotFound() {
        // Given
        when(userRepository.findByUsername("notexist")).thenReturn(Optional.empty());

        // When
        userService.findByUsername("notexist");

        // Then - expect UserNotFoundException
    }

    /**
     * US_003: testFindById_Success
     * Tìm user theo ID hợp lệ
     */
    @Test
    public void testFindById_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        User result = userService.findById(1L);

        // Then
        assertNotNull("User object should not be null", result);
        assertEquals("User ID should be 1", Long.valueOf(1L), result.getId());
        assertEquals("Username should match", "testuser", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    /**
     * US_004: testFindById_NotFound
     * Tìm user với ID không tồn tại
     */
    @Test(expected = UserNotFoundException.class)
    public void testFindById_NotFound() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        userService.findById(999L);

        // Then - expect UserNotFoundException
    }

    /**
     * US_005: testUpdateProfile_Success
     * Cập nhật profile thành công
     */
    @Test
    public void testUpdateProfile_Success() {
        // Given
        ProfileUpdateRequest request = new ProfileUpdateRequest();
        request.setFullName("Updated Name");
        request.setEmail("test@example.com");
        request.setPhone("0123456789");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.updateProfile(1L, request);

        // Then
        assertNotNull("Updated user should not be null", result);
        assertEquals("Full name should be updated", "Updated Name", result.getFullName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).saveAndFlush(any(User.class));
    }

    /**
     * US_006: testUpdateAvatar_Success
     * Cập nhật avatar thành công
     */
    @Test
    public void testUpdateAvatar_Success() {
        // Given
        String avatarUrl = "https://example.com/avatar.jpg";
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.updateAvatar(1L, avatarUrl);

        // Then
        assertNotNull("Updated user should not be null", result);
        assertEquals("Avatar URL should be updated", avatarUrl, result.getAvatarUrl());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).saveAndFlush(any(User.class));
    }
}
