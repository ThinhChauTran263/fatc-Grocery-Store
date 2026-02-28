package poly.edu.java5_asm.DevThinh;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import poly.edu.java5_asm.common.exception.CategoryNotFoundException;
import poly.edu.java5_asm.module.category.dto.response.CategoryResponse;
import poly.edu.java5_asm.module.category.entity.Category;
import poly.edu.java5_asm.module.category.repository.CategoryRepository;
import poly.edu.java5_asm.module.category.service.CategoryServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test cases cho CategoryService - Châu Thịnh
 * Test cases: CAT_001 đến CAT_006
 */
@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category rootCategory1;
    private Category rootCategory2;
    private Category rootCategory3;
    private Category childCategory1;
    private Category childCategory2;
    private Category childCategory3;
    private Category grandChildCategory1;
    private Category grandChildCategory2;
    private Category grandChildCategory3;

    @Before
    public void setUp() {
        // Root Categories (parent_id = NULL)
        rootCategory1 = Category.builder()
                .id(1L)
                .name("Departments")
                .slug("departments")
                .description("All departments")
                .iconUrl("/icons/departments.png")
                .displayOrder(1)
                .isActive(true)
                .parent(null)
                .build();

        rootCategory2 = Category.builder()
                .id(2L)
                .name("Grocery")
                .slug("grocery")
                .description("Grocery items")
                .iconUrl("/icons/grocery.png")
                .displayOrder(2)
                .isActive(true)
                .parent(null)
                .build();

        rootCategory3 = Category.builder()
                .id(3L)
                .name("Beauty")
                .slug("beauty")
                .description("Beauty products")
                .iconUrl("/icons/beauty.png")
                .displayOrder(3)
                .isActive(true)
                .parent(null)
                .build();

        // Child Categories (parent_id = 1 - Departments)
        childCategory1 = Category.builder()
                .id(4L)
                .name("Coffee")
                .slug("coffee")
                .description("Coffee products")
                .iconUrl("/icons/coffee.png")
                .displayOrder(1)
                .isActive(true)
                .parent(rootCategory1)
                .build();

        childCategory2 = Category.builder()
                .id(5L)
                .name("Electronics")
                .slug("electronics")
                .description("Electronic devices")
                .iconUrl("/icons/electronics.png")
                .displayOrder(2)
                .isActive(true)
                .parent(rootCategory1)
                .build();

        childCategory3 = Category.builder()
                .id(6L)
                .name("Clothing")
                .slug("clothing")
                .description("Clothing and accessories")
                .iconUrl("/icons/clothing.png")
                .displayOrder(3)
                .isActive(true)
                .parent(rootCategory1)
                .build();

        // Grand Child Categories (parent_id = 4 - Coffee)
        grandChildCategory1 = Category.builder()
                .id(7L)
                .name("Coffee Beans")
                .slug("coffee-beans")
                .description("Whole coffee beans")
                .iconUrl("/icons/coffee-beans.png")
                .displayOrder(1)
                .isActive(true)
                .parent(childCategory1)
                .build();

        grandChildCategory2 = Category.builder()
                .id(8L)
                .name("Ground Coffee")
                .slug("ground-coffee")
                .description("Pre-ground coffee")
                .iconUrl("/icons/ground-coffee.png")
                .displayOrder(2)
                .isActive(true)
                .parent(childCategory1)
                .build();

        grandChildCategory3 = Category.builder()
                .id(9L)
                .name("Instant Coffee")
                .slug("instant-coffee")
                .description("Instant coffee")
                .iconUrl("/icons/instant-coffee.png")
                .displayOrder(3)
                .isActive(true)
                .parent(childCategory1)
                .build();
    }

    /**
     * CAT_001: testGetAllActiveCategories_Success
     * Lấy tất cả danh mục active
     */
    @Test
    public void testGetAllActiveCategories_Success() {
        // Given
        List<Category> allCategories = Arrays.asList(
                rootCategory1, rootCategory2, rootCategory3,
                childCategory1, childCategory2, childCategory3,
                grandChildCategory1, grandChildCategory2, grandChildCategory3
        );
        
        when(categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc())
                .thenReturn(allCategories);

        // When
        List<CategoryResponse> result = categoryService.getAllActiveCategories();

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Should return 9 active categories", 9, result.size());
        
        // Verify first category
        assertEquals("First category ID should be 1", Long.valueOf(1L), result.get(0).getId());
        assertEquals("First category name should be Departments", "Departments", result.get(0).getName());
        assertEquals("First category slug should be departments", "departments", result.get(0).getSlug());
        
        verify(categoryRepository, times(1)).findByIsActiveTrueOrderByDisplayOrderAsc();
    }

    /**
     * CAT_002: testGetRootCategories_Success
     * Lấy danh mục gốc (không có parent)
     */
    @Test
    public void testGetRootCategories_Success() {
        // Given
        List<Category> rootCategories = Arrays.asList(
                rootCategory1, rootCategory2, rootCategory3
        );
        
        when(categoryRepository.findByParentIsNullAndIsActiveTrueOrderByDisplayOrderAsc())
                .thenReturn(rootCategories);

        // When
        List<CategoryResponse> result = categoryService.getRootCategories();

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Should return 3 root categories", 3, result.size());
        
        // Verify categories
        assertEquals("First root category should be Departments", "Departments", result.get(0).getName());
        assertEquals("Second root category should be Grocery", "Grocery", result.get(1).getName());
        assertEquals("Third root category should be Beauty", "Beauty", result.get(2).getName());
        
        verify(categoryRepository, times(1)).findByParentIsNullAndIsActiveTrueOrderByDisplayOrderAsc();
    }

    /**
     * CAT_003: testGetChildCategories_Success
     * Lấy danh mục con theo parentId
     */
    @Test
    public void testGetChildCategories_Success() {
        // Given
        Long parentId = 1L; // Departments
        List<Category> childCategories = Arrays.asList(
                childCategory1, childCategory2, childCategory3
        );
        
        when(categoryRepository.findByParentIdAndIsActiveTrueOrderByDisplayOrderAsc(parentId))
                .thenReturn(childCategories);

        // When
        List<CategoryResponse> result = categoryService.getChildCategories(parentId);

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Should return 3 child categories", 3, result.size());
        
        // Verify child categories
        assertEquals("First child should be Coffee", "Coffee", result.get(0).getName());
        assertEquals("Second child should be Electronics", "Electronics", result.get(1).getName());
        assertEquals("Third child should be Clothing", "Clothing", result.get(2).getName());
        
        verify(categoryRepository, times(1)).findByParentIdAndIsActiveTrueOrderByDisplayOrderAsc(parentId);
    }

    /**
     * CAT_004: testGetCategoryById_Success
     * Lấy danh mục theo ID
     */
    @Test
    public void testGetCategoryById_Success() {
        // Given
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(rootCategory1));

        // When
        CategoryResponse result = categoryService.getCategoryById(categoryId);

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Category ID should match", Long.valueOf(1L), result.getId());
        assertEquals("Category name should be Departments", "Departments", result.getName());
        assertEquals("Category slug should be departments", "departments", result.getSlug());
        assertEquals("Category icon should match", "/icons/departments.png", result.getIconUrl());
        
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    /**
     * CAT_005: testGetCategoryBySlug_Success
     * Lấy danh mục theo slug
     */
    @Test
    public void testGetCategoryBySlug_Success() {
        // Given
        String slug = "electronics";
        when(categoryRepository.findBySlug(slug))
                .thenReturn(Optional.of(childCategory2));

        // When
        CategoryResponse result = categoryService.getCategoryBySlug(slug);

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Category ID should be 5", Long.valueOf(5L), result.getId());
        assertEquals("Category name should be Electronics", "Electronics", result.getName());
        assertEquals("Category slug should be electronics", "electronics", result.getSlug());
        
        verify(categoryRepository, times(1)).findBySlug(slug);
    }

    /**
     * CAT_006: testCountActiveCategories_Success
     * Đếm số danh mục active
     */
    @Test
    public void testCountActiveCategories_Success() {
        // Given
        when(categoryRepository.countByIsActiveTrue())
                .thenReturn(9L);

        // When
        long result = categoryService.countActiveCategories();

        // Then
        assertEquals("Should return 9 active categories", 9L, result);
        
        verify(categoryRepository, times(1)).countByIsActiveTrue();
    }
}
