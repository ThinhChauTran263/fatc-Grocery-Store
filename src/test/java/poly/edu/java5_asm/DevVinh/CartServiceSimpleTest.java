package poly.edu.java5_asm.DevVinh;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import poly.edu.java5_asm.common.exception.ProductNotFoundException;
import poly.edu.java5_asm.common.exception.ProductUnavailableException;
import poly.edu.java5_asm.module.cart.dto.request.AddToCartRequest;
import poly.edu.java5_asm.module.cart.dto.request.UpdateCartItemRequest;
import poly.edu.java5_asm.module.cart.dto.response.CartResponse;
import poly.edu.java5_asm.module.cart.entity.Cart;
import poly.edu.java5_asm.module.cart.entity.CartItem;
import poly.edu.java5_asm.module.cart.repository.CartItemRepository;
import poly.edu.java5_asm.module.cart.repository.CartRepository;
import poly.edu.java5_asm.module.product.entity.Product;
import poly.edu.java5_asm.module.product.repository.ProductRepository;
import poly.edu.java5_asm.module.user.entity.User;
import poly.edu.java5_asm.module.user.repository.UserRepository;
import poly.edu.java5_asm.module.cart.service.CartServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

/**
 * Simple CartService Tests - JUnit 5
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CartService Simple Tests")
public class CartServiceSimpleTest {

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

    @BeforeEach
    public void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .isActive(true)
                .build();

        testProduct = Product.builder()
                .id(1L)
                .name("Fresh Milk")
                .slug("fresh-milk")
                .price(BigDecimal.valueOf(50000))
                .stockQuantity(100) // Thêm stock quantity
                .isActive(true)
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
                .price(BigDecimal.valueOf(50000))
                .build();
    }

    @Test
    @DisplayName("CART_001: Get Or Create Cart - Existing")
    public void testGetOrCreateCart_Existing() {
        // Given
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));

        // When
        Cart result = cartService.getOrCreateCart(testUser);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cartRepository, times(1)).findByUser(testUser);
    }

    @Test
    @DisplayName("CART_002: Get Or Create Cart - New")
    public void testGetOrCreateCart_New() {
        // Given
        Cart newCart = Cart.builder()
                .id(2L)
                .user(testUser)
                .items(new ArrayList<>())
                .build();

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(newCart);

        // When
        Cart result = cartService.getOrCreateCart(testUser);

        // Then
        assertNotNull(result);
        assertEquals(2L, result.getId());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    @DisplayName("CART_003: Get Or Create Guest Cart - Existing")
    public void testGetOrCreateGuestCart_Existing() {
        // Given
        String sessionId = "guest-123";
        Cart guestCart = Cart.builder()
                .id(3L)
                .sessionId(sessionId)
                .items(new ArrayList<>())
                .build();

        when(cartRepository.findBySessionId(sessionId)).thenReturn(Optional.of(guestCart));

        // When
        Cart result = cartService.getOrCreateGuestCart(sessionId);

        // Then
        assertNotNull(result);
        assertEquals(3L, result.getId());
    }

    @Test
    @DisplayName("CART_004: Get Or Create Guest Cart - New")
    public void testGetOrCreateGuestCart_New() {
        // Given
        String sessionId = "guest-456";
        Cart newCart = Cart.builder()
                .id(4L)
                .sessionId(sessionId)
                .items(new ArrayList<>())
                .build();

        when(cartRepository.findBySessionId(sessionId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(newCart);

        // When
        Cart result = cartService.getOrCreateGuestCart(sessionId);

        // Then
        assertNotNull(result);
        assertEquals(4L, result.getId());
    }

    @Test
    @DisplayName("CART_005: Add To Cart - Success")
    public void testAddToCart_Success() {
        // Given
        AddToCartRequest request = AddToCartRequest.builder()
                .productId(1L)
                .quantity(2)
                .build();

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(cartItemRepository.findByCartAndProduct(testCart, testProduct)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(testCartItem);
        when(cartItemRepository.findByCart(testCart)).thenReturn(new ArrayList<>());

        // When
        CartResponse response = cartService.addToCart(testUser, request);

        // Then
        assertNotNull(response);
        verify(cartRepository, times(1)).findByUser(testUser);  
        verify(productRepository, times(1)).findById(1L);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
        verify(cartItemRepository, times(1)).findByCart(testCart);
    }

    @Test
    @DisplayName("CART_006: Add To Cart - Insufficient Stock")
    public void testAddToCart_InsufficientStock() {
        // Given
        AddToCartRequest request = AddToCartRequest.builder()
                .productId(1L)
                .quantity(150) // Nhiều hơn stock (100)
                .build();

        Product lowStock = Product.builder()
                .id(1L)
                .name("Fresh Milk")
                .stockQuantity(100) // Stock thấp hơn quantity yêu cầu
                .isActive(true)
                .build();

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(lowStock));

        // When & Then - Implementation throw IllegalArgumentException, không phải ProductUnavailableException
        assertThrows(IllegalArgumentException.class, () -> {
            cartService.addToCart(testUser, request);
        });
    }

    @Test
    @DisplayName("CART_007: Add To Cart - Product Not Found")
    public void testAddToCart_ProductNotFound() {
        // Given
        AddToCartRequest request = AddToCartRequest.builder()
                .productId(999L)
                .quantity(2)
                .build();

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ProductNotFoundException.class, () -> {
            cartService.addToCart(testUser, request);
        });
    }

    @Test
    @DisplayName("CART_008: Update Cart Item - Success")
    public void testUpdateCartItem_Success() {
        // Given
        UpdateCartItemRequest request = UpdateCartItemRequest.builder()
                .cartItemId(1L)
                .quantity(3)
                .build();

        testCartItem.setQuantity(2); // Quantity cũ
        testProduct.setStockQuantity(100); // Đủ stock

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(testCartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(testCartItem);
        when(cartItemRepository.findByCart(testCart)).thenReturn(List.of(testCartItem));

        // When
        CartResponse response = cartService.updateCartItem(testUser, request);

        // Then
        assertNotNull(response);
        verify(cartItemRepository, times(1)).findById(1L);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
        assertEquals(3, testCartItem.getQuantity());
    }

    @Test
    @DisplayName("CART_009: Update Cart Item - Zero Quantity")
    public void testUpdateCartItem_ZeroQuantity() {
        // Given
        UpdateCartItemRequest request = UpdateCartItemRequest.builder()
                .cartItemId(1L)
                .quantity(0) // Quantity = 0
                .build();

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(testCartItem));

        // When & Then - Implementation throw IllegalArgumentException khi quantity <= 0
        assertThrows(IllegalArgumentException.class, () -> {
            cartService.updateCartItem(testUser, request);
        });
    }

    @Test
    @DisplayName("CART_010: Update Cart Item - Insufficient Stock")
    public void testUpdateCartItem_InsufficientStock() {
        // Given
        UpdateCartItemRequest request = UpdateCartItemRequest.builder()
                .cartItemId(1L)
                .quantity(150) // Nhiều hơn stock
                .build();

        testProduct.setStockQuantity(100); // Stock chỉ có 100

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(testCartItem));

        // When & Then - Implementation throw IllegalArgumentException khi vượt quá stock
        assertThrows(IllegalArgumentException.class, () -> {
            cartService.updateCartItem(testUser, request);
        });
    }

    @Test
    @DisplayName("CART_011: Remove From Cart - Success")
    public void testRemoveFromCart_Success() {
        // Given
        Long cartItemId = 1L;

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(testCartItem));
        doNothing().when(cartItemRepository).delete(testCartItem);
        when(cartItemRepository.findByCart(testCart)).thenReturn(new ArrayList<>());

        // When
        CartResponse response = cartService.removeFromCart(testUser, cartItemId);

        // Then
        assertNotNull(response);
        verify(cartItemRepository, times(1)).findById(cartItemId);
        verify(cartItemRepository, times(1)).delete(testCartItem);
        verify(cartItemRepository, times(1)).findByCart(testCart);
    }

    @Test
    @DisplayName("CART_012: Clear Cart - Success")
    public void testClearCart_Success() {
        // Given
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        doNothing().when(cartItemRepository).deleteByCart(testCart);

        // When
        cartService.clearCart(testUser);

        // Then
        verify(cartRepository, times(1)).findByUser(testUser);
        verify(cartItemRepository, times(1)).deleteByCart(testCart);
    }

    @Test
    @DisplayName("CART_013: Get Cart - Success")
    public void testGetCart_Success() {
        // Given
        testCart.getItems().add(testCartItem);
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));

        // When
        CartResponse result = cartService.getCart(testUser);

        // Then
        assertNotNull(result);
        verify(cartRepository, times(1)).findByUser(testUser);
    }

    @Test
    @DisplayName("CART_014: Is Cart Empty - True")
    public void testIsCartEmpty_True() {
        // Given
        Cart emptyCart = Cart.builder()
                .id(1L)
                .user(testUser)
                .items(new ArrayList<>())
                .build();

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(emptyCart));

        // When
        boolean result = cartService.isCartEmpty(testUser);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("CART_014: Is Cart Empty - False")
    public void testIsCartEmpty_False() {
        // Given
        List<CartItem> items = new ArrayList<>();
        items.add(testCartItem);
        
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findByCart(testCart)).thenReturn(items);

        // When
        boolean result = cartService.isCartEmpty(testUser);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("CART_015: Get Cart Item Count - Success")
    public void testGetCartItemCount_Success() {
        // Given
        List<CartItem> items = new ArrayList<>();
        items.add(CartItem.builder().id(1L).quantity(2).build());
        items.add(CartItem.builder().id(2L).quantity(1).build());
        items.add(CartItem.builder().id(3L).quantity(3).build());

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findByCart(testCart)).thenReturn(items);

        // When
        Integer result = cartService.getCartItemCount(testUser);

        // Then
        assertNotNull(result);
        assertEquals(3, result);
    }
}
