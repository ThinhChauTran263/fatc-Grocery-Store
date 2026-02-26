package poly.edu.java5_asm.DevNgoc;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import poly.edu.java5_asm.common.exception.ProductNotFoundException;
import poly.edu.java5_asm.common.exception.UserNotFoundException;
import poly.edu.java5_asm.common.exception.WishlistDuplicateException;
import poly.edu.java5_asm.common.exception.WishlistNotFoundException;
import poly.edu.java5_asm.module.product.entity.Product;
import poly.edu.java5_asm.module.product.repository.ProductRepository;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.module.user.repository.UserRepository;
import poly.edu.java5_asm.module.wishlist.dto.response.WishlistResponse;
import poly.edu.java5_asm.module.wishlist.entity.Wishlist;
import poly.edu.java5_asm.module.wishlist.repository.WishlistRepository;
import poly.edu.java5_asm.module.wishlist.service.WishlistServiceImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test cases cho WishlistService - Thiên Ngọc
 * Test cases: WISH_001 đến WISH_008
 */
@RunWith(MockitoJUnitRunner.class)
public class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    private User testUser;
    private Product testProduct;
    private Wishlist testWishlist;

    @Before
    public void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .fullName("Test User")
                .build();

        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(new BigDecimal("100.00"))
                .stockQuantity(10)
                .isActive(true)
                .isOutOfStock(false)
                .build();

        testWishlist = Wishlist.builder()
                .id(1L)
                .user(testUser)
                .product(testProduct)
                .build();
    }

    /**
     * WISH_001: testAddToWishlist_Success
     * Thêm sản phẩm vào wishlist
     */
    @Test
    public void testAddToWishlist_Success() {
        // Given
        when(wishlistRepository.existsByUserIdAndProductId(1L, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(testWishlist);

        // When
        WishlistResponse result = wishlistService.addToWishlist(1L, 1L);

        // Then
        assertNotNull("WishlistResponse should not be null", result);
        assertEquals("Product ID should match", Long.valueOf(1L), result.getProductId());
        verify(wishlistRepository, times(1)).existsByUserIdAndProductId(1L, 1L);
        verify(wishlistRepository, times(1)).save(any(Wishlist.class));
    }

    /**
     * WISH_002: testAddToWishlist_Duplicate
     * Thêm sản phẩm đã có trong wishlist
     */
    @Test(expected = WishlistDuplicateException.class)
    public void testAddToWishlist_Duplicate() {
        // Given
        when(wishlistRepository.existsByUserIdAndProductId(1L, 1L)).thenReturn(true);

        // When
        wishlistService.addToWishlist(1L, 1L);

        // Then - expect WishlistDuplicateException
    }

    /**
     * WISH_003: testRemoveFromWishlist_Success
     * Xóa sản phẩm khỏi wishlist
     */
    @Test
    public void testRemoveFromWishlist_Success() {
        // Given
        when(wishlistRepository.existsByUserIdAndProductId(1L, 1L)).thenReturn(true);
        doNothing().when(wishlistRepository).deleteByUserIdAndProductId(1L, 1L);

        // When
        wishlistService.removeFromWishlist(1L, 1L);

        // Then
        verify(wishlistRepository, times(1)).existsByUserIdAndProductId(1L, 1L);
        verify(wishlistRepository, times(1)).deleteByUserIdAndProductId(1L, 1L);
    }

    /**
     * WISH_004: testRemoveFromWishlist_NotFound
     * Xóa sản phẩm không có trong wishlist
     */
    @Test(expected = WishlistNotFoundException.class)
    public void testRemoveFromWishlist_NotFound() {
        // Given
        when(wishlistRepository.existsByUserIdAndProductId(1L, 1L)).thenReturn(false);

        // When
        wishlistService.removeFromWishlist(1L, 1L);

        // Then - expect WishlistNotFoundException
    }

    /**
     * WISH_005: testGetUserWishlist_Success
     * Lấy wishlist của người dùng
     */
    @Test
    public void testGetUserWishlist_Success() {
        // Given
        List<Wishlist> wishlists = Arrays.asList(testWishlist, testWishlist, testWishlist, testWishlist);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(wishlistRepository.findByUserIdWithProduct(1L)).thenReturn(wishlists);

        // When
        List<WishlistResponse> result = wishlistService.getUserWishlist(1L);

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Should return 4 items", 4, result.size());
        verify(userRepository, times(1)).existsById(1L);
        verify(wishlistRepository, times(1)).findByUserIdWithProduct(1L);
    }

    /**
     * WISH_006: testIsInWishlist_True
     * Kiểm tra sản phẩm trong wishlist - true
     */
    @Test
    public void testIsInWishlist_True() {
        // Given
        when(wishlistRepository.existsByUserIdAndProductId(1L, 1L)).thenReturn(true);

        // When
        boolean result = wishlistService.isInWishlist(1L, 1L);

        // Then
        assertTrue("Should return true", result);
        verify(wishlistRepository, times(1)).existsByUserIdAndProductId(1L, 1L);
    }

    /**
     * WISH_007: testIsInWishlist_False
     * Kiểm tra sản phẩm không trong wishlist
     */
    @Test
    public void testIsInWishlist_False() {
        // Given
        when(wishlistRepository.existsByUserIdAndProductId(1L, 2L)).thenReturn(false);

        // When
        boolean result = wishlistService.isInWishlist(1L, 2L);

        // Then
        assertFalse("Should return false", result);
        verify(wishlistRepository, times(1)).existsByUserIdAndProductId(1L, 2L);
    }

    /**
     * WISH_008: testClearWishlist_Success
     * Xóa toàn bộ wishlist
     */
    @Test
    public void testClearWishlist_Success() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);
        when(wishlistRepository.countByUserId(1L)).thenReturn(4L);
        doNothing().when(wishlistRepository).deleteByUserId(1L);

        // When
        wishlistService.clearWishlist(1L);

        // Then
        verify(userRepository, times(1)).existsById(1L);
        verify(wishlistRepository, times(1)).countByUserId(1L);
        verify(wishlistRepository, times(1)).deleteByUserId(1L);
    }
}
