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

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandServiceImpl brandService;

    private Brand brand1;
    private Brand brand2;
    private Brand brand3;
    private Brand brand4;
    private Brand brand5;

    @Before
    public void setUp() {
        // Dữ liệu thực tế từ database: 5 brands
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
     * Lấy tất cả thương hiệu active
     */
    @Test
    public void testGetAllActiveBrands_Success() {
        // Given
        List<Brand> allBrands = Arrays.asList(brand1, brand2, brand3, brand4, brand5);
        when(brandRepository.findByIsActiveTrueOrderByNameAsc())
                .thenReturn(allBrands);

        // When
        List<BrandResponse> result = brandService.getAllActiveBrands();

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Should return 5 active brands", 5, result.size());
        
        // Verify brand names
        assertEquals("First brand should be Lavazza", "Lavazza", result.get(0).getName());
        assertEquals("Second brand should be welikecoffee", "welikecoffee", result.get(1).getName());
        assertEquals("Third brand should be Starbucks", "Starbucks", result.get(2).getName());
        assertEquals("Fourth brand should be Nescafe", "Nescafe", result.get(3).getName());
        assertEquals("Fifth brand should be Trung Nguyen", "Trung Nguyen", result.get(4).getName());
        
        verify(brandRepository, times(1)).findByIsActiveTrueOrderByNameAsc();
    }

    /**
     * BRD_002: testGetBrandById_Success
     * Lấy thương hiệu theo ID
     */
    @Test
    public void testGetBrandById_Success() {
        // Given
        Long brandId = 1L;
        when(brandRepository.findById(brandId))
                .thenReturn(Optional.of(brand1));

        // When
        BrandResponse result = brandService.getBrandById(brandId);

        // Then
        assertNotNull("Result should not be null", result);
        assertEquals("Brand ID should match", Long.valueOf(1L), result.getId());
        assertEquals("Brand name should be Lavazza", "Lavazza", result.getName());
        assertEquals("Brand slug should be lavazza", "lavazza", result.getSlug());
        assertEquals("Brand logo should match", "/logos/lavazza.png", result.getLogoUrl());
        
        verify(brandRepository, times(1)).findById(brandId);
    }

    /**
     * BRD_004: testCountActiveBrands_Success
     * Đếm số thương hiệu active
     */
    @Test
    public void testCountActiveBrands_Success() {
        // Given
        when(brandRepository.countByIsActiveTrue())
                .thenReturn(5L);

        // When
        long result = brandService.countActiveBrands();

        // Then
        assertEquals("Should return 5 active brands", 5L, result);
        
        verify(brandRepository, times(1)).countByIsActiveTrue();
    }
}
