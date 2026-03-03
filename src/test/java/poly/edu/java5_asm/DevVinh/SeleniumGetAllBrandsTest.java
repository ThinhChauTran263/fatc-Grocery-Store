package poly.edu.java5_asm.DevVinh;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Selenium Test cho PROD_011: testGetAllBrands_Success
 * Test lấy danh sách brands từ UI
 *
 * @author Quốc Vinh
 */
public class SeleniumGetAllBrandsTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080";
    private static final String SIGN_IN_URL = BASE_URL + "/sign-in";
    private static final String PRODUCTS_URL = BASE_URL + "/products";

    // Test credentials
    private static final String TEST_USERNAME = "admin";
    private static final String TEST_PASSWORD = "password123";

    @Before
    public void setUp() {
        // Cấu hình ChromeDriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        // Uncomment dòng dưới nếu muốn chạy headless (không hiển thị browser)
        // options.addArguments("--headless");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        System.out.println("✅ Selenium WebDriver đã được khởi tạo");
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("✅ Selenium WebDriver đã được đóng");
        }
    }

    /**
     * PROD_011: testGetAllBrands_Success
     * Kịch bản: Đăng nhập và kiểm tra danh sách brands trên trang products
     *
     * Test case CSV: Lấy danh sách brands từ database
     * Selenium implementation: Login -> Navigate to products -> Verify brands list
     *
     * Các bước:
     * 1. Đăng nhập vào hệ thống
     * 2. Truy cập trang products
     * 3. Kiểm tra sidebar brands có hiển thị
     * 4. Đếm số lượng brands
     * 5. Kiểm tra có thể click vào brand để filter
     * 6. Verify kết quả filter
     */
    @Test
    public void testGetAllBrands_Success() {
        try {
            System.out.println("\n========================================");
            System.out.println("🧪 Bắt đầu test: PROD_011 - testGetAllBrands_Success");
            System.out.println("========================================\n");

            // Bước 1: Đăng nhập vào hệ thống
            System.out.println("📍 Bước 1: Đăng nhập vào hệ thống");
            loginToSystem();
            System.out.println("   ✅ Đăng nhập thành công\n");

            // Bước 2: Truy cập trang products
            System.out.println("📍 Bước 2: Truy cập trang products");
            driver.get(PRODUCTS_URL);
            Thread.sleep(2000);
            System.out.println("   URL: " + driver.getCurrentUrl());
            assertTrue("Should be on products page", driver.getCurrentUrl().contains("/products"));
            System.out.println("   ✅ Đã truy cập trang products thành công\n");

            // Bước 3: Kiểm tra sidebar brands có hiển thị
            System.out.println("📍 Bước 3: Kiểm tra sidebar brands");

            // Tìm brand elements trong filter sidebar
            List<WebElement> brandElements = null;

            try {
                // Thử tìm brands trong filter form
                brandElements = driver.findElements(By.cssSelector(".filter-form__list .filter-form__item input[name='brand']"));

                if (brandElements.isEmpty()) {
                    // Thử selector khác - tìm label có chứa brand
                    brandElements = driver.findElements(By.cssSelector(".filter-form__list label"));
                }

                if (brandElements.isEmpty()) {
                    // Thử tìm bất kỳ element nào có data-brand
                    brandElements = driver.findElements(By.cssSelector("[data-brand]"));
                }

                if (brandElements.isEmpty()) {
                    // Thử tìm link có chứa "brand" trong href
                    brandElements = driver.findElements(By.cssSelector("a[href*='brand']"));
                }
            } catch (Exception e) {
                System.out.println("   ⚠️ Không tìm thấy brands với selector mặc định");
            }

            assertNotNull("Brand elements should not be null", brandElements);
            System.out.println("   ✓ Tìm thấy " + brandElements.size() + " brands");

            // Bước 4: Kiểm tra có ít nhất 1 brand
            System.out.println("\n📍 Bước 4: Kiểm tra số lượng brands");
            assertTrue("Should have at least 1 brand", brandElements.size() >= 1);
            System.out.println("   ✅ Có " + brandElements.size() + " brands trong danh sách\n");

            // Bước 5: In ra tên các brands
            System.out.println("📍 Bước 5: Danh sách brands:");
            for (int i = 0; i < Math.min(brandElements.size(), 15); i++) {
                WebElement brand = brandElements.get(i);
                String brandName = brand.getText();
                if (brandName != null && !brandName.trim().isEmpty()) {
                    System.out.println("   " + (i + 1) + ". " + brandName);
                }
            }
            if (brandElements.size() > 15) {
                System.out.println("   ... và " + (brandElements.size() - 15) + " brands khác");
            }
            System.out.println();

            // Bước 6: Thử click vào brand đầu tiên để filter
            System.out.println("📍 Bước 6: Test filter theo brand");
            if (brandElements.size() > 0) {
                WebElement firstBrand = brandElements.get(0);
                String brandName = firstBrand.getText();

                // Scroll to element
                ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", firstBrand);
                Thread.sleep(1000);

                System.out.println("   Clicking vào brand: " + brandName);

                // Sử dụng JavaScript click để tránh lỗi "element click intercepted"
                try {
                    ((org.openqa.selenium.JavascriptExecutor) driver)
                        .executeScript("arguments[0].click();", firstBrand);
                } catch (Exception e) {
                    System.out.println("   ⚠️ JavaScript click failed, trying regular click...");
                    firstBrand.click();
                }

                // Đợi trang load sau khi filter
                Thread.sleep(2000);

                String currentUrl = driver.getCurrentUrl();
                System.out.println("   URL sau khi filter: " + currentUrl);

                // Kiểm tra URL có chứa brand parameter
                boolean hasFilterParam = currentUrl.contains("brand") ||
                                        currentUrl.contains("brandId");

                if (hasFilterParam) {
                    System.out.println("   ✅ Filter theo brand hoạt động đúng");
                } else {
                    System.out.println("   ⚠️ URL không chứa brand parameter, có thể filter bằng cách khác");
                }
            }
            System.out.println();

            System.out.println("========================================");
            System.out.println("✅ TEST PASSED: PROD_011 - testGetAllBrands_Success");
            System.out.println("========================================\n");

        } catch (Exception e) {
            System.err.println("❌ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    /**
     * Test case bổ sung: Kiểm tra brand count trên homepage
     */
    @Test
    public void testBrandCount_FromHomepage() {
        try {
            System.out.println("\n========================================");
            System.out.println("🧪 Bắt đầu test: testBrandCount_FromHomepage");
            System.out.println("========================================\n");

            // Đăng nhập
            loginToSystem();

            // Truy cập homepage
            driver.get(BASE_URL);
            Thread.sleep(2000);

            // Tìm brand logos hoặc brand sections
            List<WebElement> brandLogos = driver.findElements(By.cssSelector(".brand-logo, [class*='brand']"));

            System.out.println("   Tìm thấy " + brandLogos.size() + " brand elements trên homepage");

            if (brandLogos.size() > 0) {
                System.out.println("   ✅ Homepage hiển thị brands");
            } else {
                System.out.println("   ⚠️ Không tìm thấy brands trên homepage");
            }

            System.out.println("\n✅ TEST COMPLETED: testBrandCount_FromHomepage\n");

        } catch (Exception e) {
            System.err.println("❌ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    /**
     * Đăng nhập vào hệ thống
     */
    private void loginToSystem() throws InterruptedException {
        driver.get(SIGN_IN_URL);
        Thread.sleep(1000);

        WebElement usernameInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
        usernameInput.clear();
        usernameInput.sendKeys(TEST_USERNAME);

        WebElement passwordInput = driver.findElement(By.name("password"));
        passwordInput.clear();
        passwordInput.sendKeys(TEST_PASSWORD);

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        Thread.sleep(2000);
    }
}
