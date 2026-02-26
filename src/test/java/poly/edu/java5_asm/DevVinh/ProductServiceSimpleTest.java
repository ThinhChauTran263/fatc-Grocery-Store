package poly.edu.java5_asm.DevVinh;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import poly.edu.java5_asm.common.util.ProductMapper;
import poly.edu.java5_asm.module.brand.entity.Brand;
import poly.edu.java5_asm.module.brand.repository.BrandRepository;
import poly.edu.java5_asm.module.category.entity.Category;
import poly.edu.java5_asm.module.category.repository.CategoryRepository;
import poly.edu.java5_asm.module.product.dto.response.ProductListResponse;
import poly.edu.java5_asm.module.product.entity.Product;
import poly.edu.java5_asm.module.product.repository.ProductRepository;
import poly.edu.java5_asm.module.product.service.ProductServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Simple ProductService Tests - JUnit 5
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Simple Tests")
public class ProductServiceSimpleTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct1;
    private Product testProduct2;
    private Category testCategory;
    private Brand testBrand;
    private ProductListResponse productListResponse;

    @BeforeEach
    public void setUp() {
        testCategory = Category.builder()
                .id(1L)
                .name("Grocery")
                .slug("grocery")
                .isActive(true)
                .build();

        testBrand = Brand.builder()
                .id(1L)
                .name("Organic")
                .slug("organic")
                .isActive(true)
                .build();

        testProduct1 = Product.builder()
                .id(1L)
                .name("Fresh Milk")
                .slug("fresh-milk")
                .price(BigDecimal.valueOf(50000))
                .category(testCategory)
                .brand(testBrand)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        testProduct2 = Product.builder()
                .id(2L)
                .name("Organic Rice")
                .slug("organic-rice")
                .price(BigDecimal.valueOf(150000))
                .category(testCategory)
                .brand(testBrand)
                .isActive(true)
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        productListResponse = new ProductListResponse();
        productListResponse.setTotalItems(2L);
        productListResponse.setTotalPages(1);
    }

    @Test
    @DisplayName("PROD_008: Get Latest Products - Success")
    public void testGetLatestProducts_Success() {
        // Given
        List<Product> latestProducts = List.of(testProduct1, testProduct2);
        Page<Product> productPage = new PageImpl<>(latestProducts, PageRequest.of(0, 5), 2);

        when(productRepository.findLatestProducts(any(Pageable.class)))
                .thenReturn(productPage);
        when(productMapper.toProductListResponse(any(Page.class)))
                .thenReturn(productListResponse);

        // When
        ProductListResponse result = productService.getLatestProducts(0, 5);

        // Then
        assertNotNull(result);
        assertEquals(2L, result.getTotalItems());
        verify(productRepository, times(1)).findLatestProducts(any(Pageable.class));
    }

    @Test
    @DisplayName("PROD_008: Get Latest Products - Empty")
    public void testGetLatestProducts_Empty() {
        // Given
        Page<Product> emptyPage = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 5), 0);
        ProductListResponse emptyResponse = new ProductListResponse();
        emptyResponse.setTotalItems(0L);

        when(productRepository.findLatestProducts(any(Pageable.class)))
                .thenReturn(emptyPage);
        when(productMapper.toProductListResponse(any(Page.class)))
                .thenReturn(emptyResponse);

        // When
        ProductListResponse result = productService.getLatestProducts(0, 5);

        // Then
        assertNotNull(result);
        assertEquals(0L, result.getTotalItems());
    }

    @Test
    @DisplayName("PROD_009: Get Best Selling Products - Success")
    public void testGetBestSellingProducts_Success() {
        // Given
        List<Product> bestSelling = List.of(testProduct2, testProduct1);
        Page<Product> productPage = new PageImpl<>(bestSelling, PageRequest.of(0, 5), 2);

        when(productRepository.findBestSellingProducts(any(Pageable.class)))
                .thenReturn(productPage);
        when(productMapper.toProductListResponse(any(Page.class)))
                .thenReturn(productListResponse);

        // When
        ProductListResponse result = productService.getBestSellingProducts(0, 5);

        // Then
        assertNotNull(result);
        assertEquals(2L, result.getTotalItems());
    }

    @Test
    @DisplayName("PROD_010: Get All Categories - Success")
    public void testGetAllCategories_Success() {
        // Given
        List<Category> categories = List.of(testCategory);

        when(categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc()).thenReturn(categories);

        // When
        List result = productService.getAllCategories();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("PROD_011: Get All Brands - Success")
    public void testGetAllBrands_Success() {
        // Given
        List<Brand> brands = List.of(testBrand);

        when(brandRepository.findByIsActiveTrueOrderByNameAsc()).thenReturn(brands);

        // When
        List result = productService.getAllBrands();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
