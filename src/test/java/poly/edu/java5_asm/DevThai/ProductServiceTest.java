package poly.edu.java5_asm.DevThai;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import poly.edu.java5_asm.common.exception.ProductNotFoundException;
import poly.edu.java5_asm.common.util.ProductMapper;
import poly.edu.java5_asm.module.brand.entity.Brand;
import poly.edu.java5_asm.module.brand.repository.BrandRepository;
import poly.edu.java5_asm.module.category.entity.Category;
import poly.edu.java5_asm.module.category.repository.CategoryRepository;
import poly.edu.java5_asm.module.product.dto.request.ProductSearchRequest;
import poly.edu.java5_asm.module.product.dto.response.ProductListResponse;
import poly.edu.java5_asm.module.product.dto.response.ProductResponse;
import poly.edu.java5_asm.module.product.entity.Product;
import poly.edu.java5_asm.module.product.repository.ProductRepository;
import poly.edu.java5_asm.module.product.service.ProductServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test cases cho ProductService - Quốc Thái
 * Test cases: PROD_001 đến PROD_007
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

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

    private Product testProduct;
    private Category testCategory;
    private Brand testBrand;
    private ProductResponse productResponse;
    private ProductListResponse productListResponse;

    @Before
    public void setUp() {
        testCategory = Category.builder()
                .id(1L)
                .name("Electronics")
                .slug("electronics")
                .isActive(true)
                .build();

        testBrand = Brand.builder()
                .id(1L)
                .name("Apple")
                .slug("apple")
                .isActive(true)
                .build();

        testProduct = Product.builder()
                .id(1L)
                .name("Laptop")
                .slug("laptop")
                .price(BigDecimal.valueOf(1000))
                .category(testCategory)
                .brand(testBrand)
                .isActive(true)
                .isFeatured(true)
                .build();

        productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setName("Laptop");

        productListResponse = new ProductListResponse();
        productListResponse.setTotalItems(1L);
        productListResponse.setTotalPages(1);
    }

    /**
     * PROD_001: testSearchAndFilterProducts_Success
     * Tìm kiếm sản phẩm thành công
     */
    @Test
    public void testSearchAndFilterProducts_Success() {
        // Given
        ProductSearchRequest request = new ProductSearchRequest();
        request.setKeyword("Laptop");
        request.setPage(0);
        request.setSize(10);
        request.setSortBy("name");
        request.setSortDirection("asc");

        List<Product> products = List.of(testProduct);
        Page<Product> productPage = new PageImpl<>(products);

        when(productRepository.searchAndFilter(
                eq("Laptop"), isNull(), isNull(), isNull(), isNull(), any(Pageable.class)
        )).thenReturn(productPage);
        when(productMapper.toProductListResponse(any(Page.class), isNull()))
                .thenReturn(productListResponse);

        // When
        ProductListResponse result = productService.searchAndFilterProducts(request, null);

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Total items should be 1", 1L, result.getTotalItems());
        verify(productRepository, times(1)).searchAndFilter(
                eq("Laptop"), isNull(), isNull(), isNull(), isNull(), any(Pageable.class)
        );
    }

    /**
     * PROD_002: testSearchAndFilterProducts_WithCategory
     * Lọc sản phẩm theo category
     */
    @Test
    public void testSearchAndFilterProducts_WithCategory() {
        // Given
        ProductSearchRequest request = new ProductSearchRequest();
        request.setCategoryId(1L);
        request.setPage(0);
        request.setSize(10);
        request.setSortBy("name");
        request.setSortDirection("asc");

        List<Product> products = List.of(testProduct);
        Page<Product> productPage = new PageImpl<>(products);

        when(productRepository.searchAndFilter(
                isNull(), eq(1L), isNull(), isNull(), isNull(), any(Pageable.class)
        )).thenReturn(productPage);
        when(productMapper.toProductListResponse(any(Page.class), isNull()))
                .thenReturn(productListResponse);

        // When
        ProductListResponse result = productService.searchAndFilterProducts(request, null);

        // Then
        assertNotNull("Result should not be null", result);
        verify(productRepository, times(1)).searchAndFilter(
                isNull(), eq(1L), isNull(), isNull(), isNull(), any(Pageable.class)
        );
    }

    /**
     * PROD_003: testSearchAndFilterProducts_WithPriceRange
     * Lọc sản phẩm theo khoảng giá
     */
    @Test
    public void testSearchAndFilterProducts_WithPriceRange() {
        // Given
        ProductSearchRequest request = new ProductSearchRequest();
        request.setMinPrice(BigDecimal.valueOf(100));
        request.setMaxPrice(BigDecimal.valueOf(500));
        request.setPage(0);
        request.setSize(10);
        request.setSortBy("price");
        request.setSortDirection("asc");

        List<Product> products = List.of(testProduct);
        Page<Product> productPage = new PageImpl<>(products);

        when(productRepository.searchAndFilter(
                isNull(), isNull(), isNull(), 
                eq(BigDecimal.valueOf(100)), eq(BigDecimal.valueOf(500)), 
                any(Pageable.class)
        )).thenReturn(productPage);
        when(productMapper.toProductListResponse(any(Page.class), isNull()))
                .thenReturn(productListResponse);

        // When
        ProductListResponse result = productService.searchAndFilterProducts(request, null);

        // Then
        assertNotNull("Result should not be null", result);
        verify(productRepository, times(1)).searchAndFilter(
                isNull(), isNull(), isNull(), 
                eq(BigDecimal.valueOf(100)), eq(BigDecimal.valueOf(500)), 
                any(Pageable.class)
        );
    }

    /**
     * PROD_004: testGetProductById_Success
     * Lấy chi tiết sản phẩm theo ID
     */
    @Test
    public void testGetProductById_Success() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        // When
        ProductResponse result = productService.getProductById(1L);

        // Then
        assertNotNull("Product response should not be null", result);
        assertEquals("Product ID should be 1", Long.valueOf(1L), result.getId());
        assertEquals("Product name should match", "Laptop", result.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    /**
     * PROD_005: testGetProductById_NotFound
     * Lấy sản phẩm không tồn tại
     */
    @Test(expected = ProductNotFoundException.class)
    public void testGetProductById_NotFound() {
        // Given
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        productService.getProductById(999L);

        // Then - expect ProductNotFoundException
    }

    /**
     * PROD_006: testGetAllProducts_Success
     * Lấy tất cả sản phẩm có phân trang
     */
    @Test
    public void testGetAllProducts_Success() {
        // Given
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            products.add(testProduct);
        }
        Page<Product> productPage = new PageImpl<>(products, PageRequest.of(0, 10), 20);

        when(productRepository.findByIsActiveTrue(any(Pageable.class))).thenReturn(productPage);
        when(productMapper.toProductListResponse(any(Page.class))).thenReturn(productListResponse);

        // When
        ProductListResponse result = productService.getAllProducts(0, 10, "name", "asc");

        // Then
        assertNotNull("Result should not be null", result);
        verify(productRepository, times(1)).findByIsActiveTrue(any(Pageable.class));
    }

    /**
     * PROD_007: testGetFeaturedProducts_Success
     * Lấy sản phẩm nổi bật
     */
    @Test
    public void testGetFeaturedProducts_Success() {
        // Given
        List<Product> products = List.of(testProduct);
        Page<Product> productPage = new PageImpl<>(products, PageRequest.of(0, 5), 5);

        when(productRepository.findByIsFeaturedTrueAndIsActiveTrue(any(Pageable.class)))
                .thenReturn(productPage);
        when(productMapper.toProductListResponse(any(Page.class))).thenReturn(productListResponse);

        // When
        ProductListResponse result = productService.getFeaturedProducts(0, 5);

        // Then
        assertNotNull("Result should not be null", result);
        verify(productRepository, times(1)).findByIsFeaturedTrueAndIsActiveTrue(any(Pageable.class));
    }
}
