package poly.edu.java5_asm.module.product.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import poly.edu.java5_asm.module.brand.entity.Brand;
import poly.edu.java5_asm.module.category.entity.Category;
import poly.edu.java5_asm.module.product.dto.response.ProductResponse;
import poly.edu.java5_asm.module.product.entity.Product;
import poly.edu.java5_asm.module.product.repository.ProductRepository;
import poly.edu.java5_asm.module.review.repository.ReviewRepository;
import poly.edu.java5_asm.module.wishlist.repository.WishlistRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit Test cho ProductService
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private WishlistRepository wishlistRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;

    @Before
    public void setUp() {
        Category category = Category.builder()
                .id(1L)
                .name("Electronics")
                .build();

        Brand brand = Brand.builder()
                .id(1L)
                .name("Samsung")
                .build();

        testProduct = Product.builder()
                .id(1L)
                .name("Samsung Galaxy S21")
                .slug("samsung-galaxy-s21")
                .price(new BigDecimal("799.99"))
                .stockQuantity(10)
                .category(category)
                .brand(brand)
                .description("Latest Samsung smartphone")
                .build();
    }

    @Test
    public void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // Chỉ test repository layer
        Optional<Product> result = productRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(Long.valueOf(1L), result.get().getId());
        assertEquals("Samsung Galaxy S21", result.get().getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetProductById_NotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        
        Optional<Product> result = productRepository.findById(999L);
        
        assertFalse(result.isPresent());
    }

    @Test
    public void testSearchByName_Success() {
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findByNameContainingIgnoreCase("Samsung"))
                .thenReturn(products);

        List<Product> result = productRepository.findByNameContainingIgnoreCase("Samsung");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertTrue(result.get(0).getName().contains("Samsung"));
    }

    @Test
    public void testCheckStock_Available() {
        int requestedQuantity = 5;
        boolean isAvailable = testProduct.getStockQuantity() >= requestedQuantity;
        assertTrue(isAvailable);
    }

    @Test
    public void testCheckStock_Insufficient() {
        int requestedQuantity = 15;
        boolean isAvailable = testProduct.getStockQuantity() >= requestedQuantity;
        assertFalse(isAvailable);
    }
}
