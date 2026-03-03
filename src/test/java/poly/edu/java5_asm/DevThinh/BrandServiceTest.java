package poly.edu.java5_asm.DevThinh;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import poly.edu.java5_asm.common.exception.BrandNotFoundException;
import poly.edu.java5_asm.module.brand.dto.response.BrandResponse;
import poly.edu.java5_asm.module.brand.entity.Brand;
import poly.edu.java5_asm.module.brand.repository.BrandRepository;
import poly.edu.java5_asm.module.brand.service.BrandServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test cases cho BrandService - Châu Thịnh
 * Test cases: BRD_001, BRD_002, BRD_004
 * (BRD_003 được test bằng Selenium)
 */
@RunWith(MockitoJUnitRunner.class)
public class BrandServiceTest {

    // @Mock: Tạo đối tượng giả lập (fake) của BrandRepository
    // Không kết nối database thật, chỉ giả lập hành vi
    @Mock
    private BrandRepository brandRepository;

    // @InjectMocks: Tự động inject (tiêm) mock repository vào service
    // Giống như @Autowired nhưng dùng cho test
    @InjectMocks
    private BrandServiceImpl brandService;

    // Khai báo các biến brand để dùng trong test
    private Brand brand1;
    private Brand brand2;
    private Brand brand3;
    private Brand brand4;
    private Brand brand5;

    /**
     * @Before: Method này chạy TRƯỚC MỖI test case
     * Mục đích: Chuẩn bị dữ liệu giả lập (mock data) cho test
     * Tại sao cần: Vì dùng @Mock nên không có database thật, phải tự tạo data
     */
    @Before
    public void setUp() {
        // Tạo 5 brand giả lập giống dữ liệu thực tế trong database
        brand1 = Brand.builder()
                .id(1L)
                .name("Lavazza")
                .slug("lavazza")
                .description("Italian coffee brand")
                .logoUrl("/logos/lavazza.png")
                .isActive(true)
                .build();

        brand2 = Brand.builder()
                .id(2L)
                .name("welikecoffee")
                .slug("welikecoffee")
                .description("Premium coffee brand")
                .logoUrl("/logos/welikecoffee.png")
                .isActive(true)
                .build();

        brand3 = Brand.builder()
                .id(3L)
                .name("Starbucks")
                .slug("starbucks")
                .description("Global coffee chain")
                .logoUrl("/logos/starbucks.png")
                .isActive(true)
                .build();

        brand4 = Brand.builder()
                .id(4L)
                .name("Nescafe")
                .slug("nescafe")
                .description("Instant coffee brand")
                .logoUrl("/logos/nescafe.png")
                .isActive(true)
                .build();

        brand5 = Brand.builder()
                .id(5L)
                .name("Trung Nguyen")
                .slug("trung-nguyen")
                .description("Vietnamese coffee brand")
                .logoUrl("/logos/trung-nguyen.png")
                .isActive(true)
                .build();
    }

    /**
     * BRD_001: testGetAllActiveBrands_Success
     * Mục đích: Test lấy tất cả thương hiệu đang hoạt động (active)
     * Kỳ vọng: Trả về 5 brands theo thứ tự tên
     */
    @Test
    public void testGetAllActiveBrands_Success() {
        // BƯỚC 1 - GIVEN (Chuẩn bị): Giả lập repository trả về 5 brands
        List<Brand> allBrands = Arrays.asList(brand1, brand2, brand3, brand4, brand5);
        
        // when().thenReturn(): Khi gọi method này → trả về kết quả đó
        // Giả lập: Khi gọi findByIsActiveTrueOrderByNameAsc() → trả về allBrands
        when(brandRepository.findByIsActiveTrueOrderByNameAsc())
                .thenReturn(allBrands);

        // BƯỚC 2 - WHEN (Thực hiện): Gọi method cần test
        List<BrandResponse> result = brandService.getAllActiveBrands();

        // BƯỚC 3 - THEN (Kiểm tra): Verify kết quả có đúng không
        // Kiểm tra kết quả không null
        assertNotNull("Kết quả không được null", result);
        
        // Kiểm tra có đúng 5 brands
        assertEquals("Phải trả về 5 thương hiệu đang hoạt động", 5, result.size());
        
        // Kiểm tra tên từng brand có đúng không
        assertEquals("Thương hiệu đầu tiên phải là Lavazza", "Lavazza", result.get(0).getName());
        assertEquals("Thương hiệu thứ hai phải là welikecoffee", "welikecoffee", result.get(1).getName());
        assertEquals("Thương hiệu thứ ba phải là Starbucks", "Starbucks", result.get(2).getName());
        assertEquals("Thương hiệu thứ tư phải là Nescafe", "Nescafe", result.get(3).getName());
        assertEquals("Thương hiệu thứ năm phải là Trung Nguyen", "Trung Nguyen", result.get(4).getName());
        
        // verify(): Kiểm tra method repository có được gọi đúng 1 lần không
        verify(brandRepository, times(1)).findByIsActiveTrueOrderByNameAsc();
    }

    /**
     * BRD_002: testGetBrandById_Success
     * Mục đích: Test lấy thông tin thương hiệu theo ID
     * Kỳ vọng: Trả về đúng thông tin brand có ID = 1 (Lavazza)
     */
    @Test
    public void testGetBrandById_Success() {
        // GIVEN: Giả lập tìm brand theo ID = 1
        Long brandId = 1L;
        when(brandRepository.findById(brandId))
                .thenReturn(Optional.of(brand1)); // Trả về brand1 (Lavazza)

        // WHEN: Gọi service để lấy brand
        BrandResponse result = brandService.getBrandById(brandId);

        // THEN: Kiểm tra thông tin brand có đúng không
        assertNotNull("Kết quả không được null", result);
        assertEquals("ID thương hiệu phải khớp", Long.valueOf(1L), result.getId());
        assertEquals("Tên thương hiệu phải là Lavazza", "Lavazza", result.getName());
        assertEquals("Slug thương hiệu phải là lavazza", "lavazza", result.getSlug());
        assertEquals("Logo thương hiệu phải khớp", "/logos/lavazza.png", result.getLogoUrl());
        
        // Verify repository được gọi đúng 1 lần với brandId
        verify(brandRepository, times(1)).findById(brandId);
    }

    /**
     * BRD_004: testCountActiveBrands_Success
     * Mục đích: Test đếm số lượng thương hiệu đang hoạt động
     * Kỳ vọng: Trả về 5 (có 5 brands active)
     */
    @Test
    public void testCountActiveBrands_Success() {
        // GIVEN: Giả lập repository đếm được 5 brands active
        when(brandRepository.countByIsActiveTrue())
                .thenReturn(5L); // Trả về 5

        // WHEN: Gọi service để đếm
        long result = brandService.countActiveBrands();

        // THEN: Kiểm tra kết quả có đúng 5 không
        assertEquals("Phải trả về 5 thương hiệu đang hoạt động", 5L, result);
        
        // Verify repository được gọi đúng 1 lần
        verify(brandRepository, times(1)).countByIsActiveTrue();
    }
}
