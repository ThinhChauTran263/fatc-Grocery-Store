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

    // @Mock: Tạo đối tượng giả lập (fake) của CategoryRepository
    @Mock
    private CategoryRepository categoryRepository;

    // @InjectMocks: Tự động inject mock repository vào service
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

    /**
     * @Before: Chạy trước mỗi test case
     * Chuẩn bị dữ liệu giả lập: 3 cấp danh mục (Root → Child → Grand Child)
     */
    @Before
    public void setUp() {
        // Cấp 1: Root Categories (danh mục gốc, không có parent)
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

        // Cấp 2: Child Categories (danh mục con của Departments)
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

        // Cấp 3: Grand Child Categories (danh mục cháu của Coffee)
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
     * Mục đích: Test lấy tất cả danh mục đang hoạt động
     * Kỳ vọng: Trả về 9 danh mục (3 root + 3 child + 3 grand child)
     */
    @Test
    public void testGetAllActiveCategories_Success() {
        // GIVEN: Giả lập repository trả về 9 danh mục
        List<Category> allCategories = Arrays.asList(
                rootCategory1, rootCategory2, rootCategory3,
                childCategory1, childCategory2, childCategory3,
                grandChildCategory1, grandChildCategory2, grandChildCategory3
        );
        
        when(categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc())
                .thenReturn(allCategories);

        // WHEN: Gọi service để lấy tất cả danh mục
        List<CategoryResponse> result = categoryService.getAllActiveCategories();

        // THEN: Kiểm tra kết quả
        assertNotNull("Kết quả không được null", result);
        assertEquals("Phải trả về 9 danh mục đang hoạt động", 9, result.size());
        
        // Kiểm tra danh mục đầu tiên
        assertEquals("ID danh mục đầu tiên phải là 1", Long.valueOf(1L), result.get(0).getId());
        assertEquals("Tên danh mục đầu tiên phải là Departments", "Departments", result.get(0).getName());
        assertEquals("Slug danh mục đầu tiên phải là departments", "departments", result.get(0).getSlug());
        
        verify(categoryRepository, times(1)).findByIsActiveTrueOrderByDisplayOrderAsc();
    }

    /**
     * CAT_002: testGetRootCategories_Success
     * Mục đích: Test lấy danh mục gốc (không có parent)
     * Kỳ vọng: Trả về 3 danh mục gốc (Departments, Grocery, Beauty)
     */
    @Test
    public void testGetRootCategories_Success() {
        // GIVEN: Giả lập repository trả về 3 danh mục gốc
        List<Category> rootCategories = Arrays.asList(
                rootCategory1, rootCategory2, rootCategory3
        );
        
        when(categoryRepository.findByParentIsNullAndIsActiveTrueOrderByDisplayOrderAsc())
                .thenReturn(rootCategories);

        // WHEN: Gọi service để lấy danh mục gốc
        List<CategoryResponse> result = categoryService.getRootCategories();

        // THEN: Kiểm tra kết quả
        assertNotNull("Kết quả không được null", result);
        assertEquals("Phải trả về 3 danh mục gốc", 3, result.size());
        
        // Kiểm tra tên từng danh mục gốc
        assertEquals("Danh mục gốc đầu tiên phải là Departments", "Departments", result.get(0).getName());
        assertEquals("Danh mục gốc thứ hai phải là Grocery", "Grocery", result.get(1).getName());
        assertEquals("Danh mục gốc thứ ba phải là Beauty", "Beauty", result.get(2).getName());
        
        verify(categoryRepository, times(1)).findByParentIsNullAndIsActiveTrueOrderByDisplayOrderAsc();
    }

    /**
     * CAT_003: testGetChildCategories_Success
     * Mục đích: Test lấy danh mục con theo parentId
     * Kỳ vọng: Trả về 3 danh mục con của Departments (Coffee, Electronics, Clothing)
     */
    @Test
    public void testGetChildCategories_Success() {
        // GIVEN: Giả lập lấy danh mục con của Departments (ID=1)
        Long parentId = 1L; // Departments
        List<Category> childCategories = Arrays.asList(
                childCategory1, childCategory2, childCategory3
        );
        
        when(categoryRepository.findByParentIdAndIsActiveTrueOrderByDisplayOrderAsc(parentId))
                .thenReturn(childCategories);

        // WHEN: Gọi service để lấy danh mục con
        List<CategoryResponse> result = categoryService.getChildCategories(parentId);

        // THEN: Kiểm tra kết quả
        assertNotNull("Kết quả không được null", result);
        assertEquals("Phải trả về 3 danh mục con", 3, result.size());
        
        // Kiểm tra tên từng danh mục con
        assertEquals("Danh mục con đầu tiên phải là Coffee", "Coffee", result.get(0).getName());
        assertEquals("Danh mục con thứ hai phải là Electronics", "Electronics", result.get(1).getName());
        assertEquals("Danh mục con thứ ba phải là Clothing", "Clothing", result.get(2).getName());
        
        verify(categoryRepository, times(1)).findByParentIdAndIsActiveTrueOrderByDisplayOrderAsc(parentId);
    }

    /**
     * CAT_004: testGetCategoryById_Success
     * Mục đích: Test lấy danh mục theo ID
     * Kỳ vọng: Trả về đúng thông tin danh mục Departments (ID=1)
     */
    @Test
    public void testGetCategoryById_Success() {
        // GIVEN: Giả lập tìm danh mục theo ID=1
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(rootCategory1));

        // WHEN: Gọi service để lấy danh mục
        CategoryResponse result = categoryService.getCategoryById(categoryId);

        // THEN: Kiểm tra thông tin danh mục
        assertNotNull("Kết quả không được null", result);
        assertEquals("ID danh mục phải khớp", Long.valueOf(1L), result.getId());
        assertEquals("Tên danh mục phải là Departments", "Departments", result.getName());
        assertEquals("Slug danh mục phải là departments", "departments", result.getSlug());
        assertEquals("Icon danh mục phải khớp", "/icons/departments.png", result.getIconUrl());
        
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    /**
     * CAT_005: testGetCategoryBySlug_Success
     * Mục đích: Test lấy danh mục theo slug
     * Kỳ vọng: Trả về đúng thông tin danh mục Electronics (slug="electronics")
     */
    @Test
    public void testGetCategoryBySlug_Success() {
        // GIVEN: Giả lập tìm danh mục theo slug="electronics"
        String slug = "electronics";
        when(categoryRepository.findBySlug(slug))
                .thenReturn(Optional.of(childCategory2));

        // WHEN: Gọi service để lấy danh mục
        CategoryResponse result = categoryService.getCategoryBySlug(slug);

        // THEN: Kiểm tra thông tin danh mục
        assertNotNull("Kết quả không được null", result);
        assertEquals("ID danh mục phải là 5", Long.valueOf(5L), result.getId());
        assertEquals("Tên danh mục phải là Electronics", "Electronics", result.getName());
        assertEquals("Slug danh mục phải là electronics", "electronics", result.getSlug());
        
        verify(categoryRepository, times(1)).findBySlug(slug);
    }

    /**
     * CAT_006: testCountActiveCategories_Success
     * Mục đích: Test đếm số lượng danh mục đang hoạt động
     * Kỳ vọng: Trả về 9 (có 9 danh mục active)
     */
    @Test
    public void testCountActiveCategories_Success() {
        // GIVEN: Giả lập repository đếm được 9 danh mục active
        when(categoryRepository.countByIsActiveTrue())
                .thenReturn(9L);

        // WHEN: Gọi service để đếm
        long result = categoryService.countActiveCategories();

        // THEN: Kiểm tra kết quả
        assertEquals("Phải trả về 9 danh mục đang hoạt động", 9L, result);
        
        verify(categoryRepository, times(1)).countByIsActiveTrue();
    }
}
