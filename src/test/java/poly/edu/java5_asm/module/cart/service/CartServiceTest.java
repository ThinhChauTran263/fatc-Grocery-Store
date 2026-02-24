package poly.edu.java5_asm.module.cart.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import poly.edu.java5_asm.module.cart.dto.request.AddToCartRequest;
import poly.edu.java5_asm.module.cart.dto.response.CartResponse;
import poly.edu.java5_asm.module.cart.entity.Cart;
import poly.edu.java5_asm.module.cart.entity.CartItem;
import poly.edu.java5_asm.module.cart.repository.CartItemRepository;
import poly.edu.java5_asm.module.cart.repository.CartRepository;
import poly.edu.java5_asm.module.product.entity.Product;
import poly.edu.java5_asm.module.product.repository.ProductRepository;
import poly.edu.java5_asm.module.user.entity.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Test cho CartService
 */
@RunWith(MockitoJUnitRunner.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

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
                .build();
    }

    @Test
    public void testAddToCart_Success() {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(2);

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(cartItemRepository.findByCartAndProduct(testCart, testProduct))
                .thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(testCartItem);

        CartResponse result = cartService.addToCart(testUser, request);

        assertNotNull(result);
        verify(productRepository, times(1)).findById(1L);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test(expected = RuntimeException.class)
    public void testAddToCart_ProductNotFound() {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(999L);
        request.setQuantity(1);

        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        cartService.addToCart(testUser, request);
    }

    @Test
    public void testGetOrCreateCart_ExistingCart() {
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));

        Cart result = cartService.getOrCreateCart(testUser);

        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getId());
        verify(cartRepository, times(1)).findByUser(testUser);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    public void testGetOrCreateCart_NewCart() {
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        Cart result = cartService.getOrCreateCart(testUser);

        assertNotNull(result);
        verify(cartRepository, times(1)).findByUser(testUser);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void testCartItem_ValidQuantity() {
        assertTrue(testCartItem.getQuantity() > 0);
        assertEquals(Integer.valueOf(2), testCartItem.getQuantity());
    }
}
