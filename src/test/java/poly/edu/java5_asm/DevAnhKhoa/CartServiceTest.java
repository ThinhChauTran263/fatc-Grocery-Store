package poly.edu.java5_asm.DevAnhKhoa;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import poly.edu.java5_asm.module.cart.dto.response.CartResponse;
import poly.edu.java5_asm.module.cart.entity.Cart;
import poly.edu.java5_asm.module.cart.entity.CartItem;
import poly.edu.java5_asm.module.cart.repository.CartItemRepository;
import poly.edu.java5_asm.module.cart.repository.CartRepository;
import poly.edu.java5_asm.module.cart.service.CartServiceImpl;
import poly.edu.java5_asm.module.product.entity.Product;
import poly.edu.java5_asm.module.product.repository.ProductRepository;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.module.user.repository.UserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test cases cho CartService - Anh Khoa
 * Test cases: CART_017, CART_018
 */
@RunWith(MockitoJUnitRunner.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private User testUser;
    private Product testProduct;
    private Cart testCart;
    private CartItem testCartItem;

    @Before
    public void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(new BigDecimal("100.00"))
                .stockQuantity(10)
                .build();

        testCart = Cart.builder()
                .id(1L)
                .user(testUser)
                .items(new ArrayList<>())
                .build();

        testCartItem = CartItem.builder()
                .id(1L)
                .cart(testCart)
                .product(testProduct)
                .quantity(2)
                .price(new BigDecimal("100.00"))
                .build();
    }

    /**
     * CART_017: testApplyPromoCode_Invalid
     * Áp dụng mã giảm giá không hợp lệ
     */
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPromoCode_Invalid() {
        // Given
        String identifier = "guest_123";
        String sessionId = "123";
        String invalidPromoCode = "INVALID";

        when(cartRepository.findBySessionId(sessionId)).thenReturn(Optional.of(testCart));

        // When
        cartService.applyPromoCode(identifier, invalidPromoCode);

        // Then - expect IllegalArgumentException
    }

    /**
     * CART_018: testRemovePromoCode_Success
     * Xóa mã giảm giá
     */
    @Test
    public void testRemovePromoCode_Success() {
        // Given
        String identifier = "guest_123";
        String sessionId = "123";
        testCart.setPromoCode("SAVE10");
        
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(testCartItem);

        when(cartRepository.findBySessionId(sessionId)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findByCart(testCart)).thenReturn(cartItems);
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // When
        CartResponse result = cartService.removePromoCode(identifier);

        // Then
        assertNotNull("CartResponse should not be null", result);
        assertNull("Promo code should be removed", testCart.getPromoCode());
        verify(cartRepository, times(1)).findBySessionId(sessionId);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }
}
