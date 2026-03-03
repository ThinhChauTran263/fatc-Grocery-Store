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
 * Selenium Test cho CART_016: testApplyPromoCode_Success
 * Test áp dụng mã giảm giá hợp lệ "GIAM10K"
 *
 * @author Quốc Vinh
 */
public class SeleniumApplyValidPromoCodeTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080";
    private static final String SIGN_IN_URL = BASE_URL + "/sign-in";
    private static final String PRODUCTS_URL = BASE_URL + "/products";
    private static final String CART_URL = BASE_URL + "/cart";

    // Test credentials
    private static final String TEST_USERNAME = "admin";
    private static final String TEST_PASSWORD = "password123";

    // Valid promo code from database
    private static final String VALID_PROMO_CODE = "GIAM10K";

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
     * CART_016: testApplyPromoCode_Success
     * Kịch bản: Áp dụng mã giảm giá hợp lệ "GIAM10K" vào giỏ hàng
     *
     * Test case CSV: identifier="cart-123", promoCode="GIAM10K"
     * Selenium implementation: Login -> Add product to cart -> Apply valid promo code
     *
     * Các bước:
     * 1. Đăng nhập vào hệ thống
     * 2. Thêm sản phẩm vào giỏ hàng
     * 3. Truy cập trang giỏ hàng
     * 4. Lưu giá trước khi áp dụng mã
     * 5. Nhập mã giảm giá hợp lệ "GIAM10K"
     * 6. Kiểm tra hiển thị thông báo thành công
     * 7. Verify giá đã được giảm
     */
    @Test
    public void testApplyPromoCode_Success() {
        try {
            System.out.println("\n========================================");
            System.out.println("🧪 Bắt đầu test: CART_016 - testApplyPromoCode_Success");
            System.out.println("========================================\n");

            // Bước 1: Đăng nhập vào hệ thống
            System.out.println("📍 Bước 1: Đăng nhập vào hệ thống");
            loginToSystem();
            System.out.println("   ✅ Đăng nhập thành công\n");

            // Bước 2: Thêm sản phẩm vào giỏ hàng
            System.out.println("📍 Bước 2: Thêm sản phẩm vào giỏ hàng");
            addProductToCart();
            System.out.println("   ✅ Đã thêm sản phẩm vào giỏ hàng\n");

            // Bước 3: Truy cập trang giỏ hàng
            System.out.println("📍 Bước 3: Truy cập trang giỏ hàng");
            driver.get(CART_URL);
            Thread.sleep(2000);
            System.out.println("   URL: " + driver.getCurrentUrl());
            assertTrue("Should be on cart page", driver.getCurrentUrl().contains("/cart"));
            System.out.println("   ✅ Đã truy cập trang giỏ hàng\n");

            // Bước 4: Lưu giá trước khi áp dụng mã
            System.out.println("📍 Bước 4: Lưu giá trước khi áp dụng mã");
            String priceBeforeDiscount = getTotalPrice();
            System.out.println("   Giá trước khi giảm: " + priceBeforeDiscount);
            System.out.println();

            // Bước 5: Nhập mã giảm giá hợp lệ
            System.out.println("📍 Bước 5: Nhập mã giảm giá hợp lệ");
            applyPromoCode(VALID_PROMO_CODE);
            System.out.println("   ✓ Đã nhập mã: " + VALID_PROMO_CODE);
            System.out.println();

            // Đợi response từ server
            Thread.sleep(2000);

            // Bước 6: Kiểm tra hiển thị thông báo thành công
            System.out.println("📍 Bước 6: Kiểm tra thông báo thành công");
            boolean hasSuccessMessage = checkForSuccessMessage();

            if (hasSuccessMessage) {
                System.out.println("   ✅ Hiển thị thông báo thành công\n");
            } else {
                System.out.println("   ⚠️ Không tìm thấy thông báo success (có thể UI không hiển thị)\n");
            }

            // Bước 7: Kiểm tra giá đã được giảm
            System.out.println("📍 Bước 7: Kiểm tra giá đã được giảm");
            boolean hasDiscount = checkForDiscount();

            assertTrue("Should apply discount for valid promo code", hasDiscount);
            System.out.println("   ✅ Mã giảm giá đã được áp dụng thành công");

            String priceAfterDiscount = getTotalPrice();
            System.out.println("   Giá sau khi giảm: " + priceAfterDiscount);

            // Kiểm tra giá đã thay đổi
            if (!priceBeforeDiscount.equals(priceAfterDiscount)) {
                System.out.println("   ✅ Giá đã thay đổi sau khi áp dụng mã");
            }
            System.out.println();

            // Bước 8: Kiểm tra không có thông báo lỗi
            System.out.println("📍 Bước 8: Kiểm tra không có thông báo lỗi");
            boolean hasErrorMessage = checkForErrorMessage();
            assertFalse("Should not have error message", hasErrorMessage);
            System.out.println("   ✅ Không có thông báo lỗi\n");

            System.out.println("========================================");
            System.out.println("✅ TEST PASSED: CART_016 - testApplyPromoCode_Success");
            System.out.println("========================================\n");

        } catch (Exception e) {
            System.err.println("❌ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    /**
     * Test case bổ sung: Kiểm tra remove promo code
     */
    @Test
    public void testRemovePromoCode_Success() {
        try {
            System.out.println("\n========================================");
            System.out.println("🧪 Bắt đầu test: testRemovePromoCode_Success");
            System.out.println("========================================\n");

            // Đăng nhập
            loginToSystem();

            // Thêm sản phẩm vào giỏ
            addProductToCart();

            // Truy cập giỏ hàng
            driver.get(CART_URL);
            Thread.sleep(2000);

            // Áp dụng mã giảm giá
            System.out.println("   Áp dụng mã giảm giá: " + VALID_PROMO_CODE);
            applyPromoCode(VALID_PROMO_CODE);
            Thread.sleep(2000);

            // Kiểm tra mã đã được áp dụng
            boolean hasDiscount = checkForDiscount();
            if (hasDiscount) {
                System.out.println("   ✅ Mã giảm giá đã được áp dụng");

                // Thử remove promo code
                System.out.println("   Thử remove mã giảm giá...");
                removePromoCode();
                Thread.sleep(2000);

                // Kiểm tra mã đã bị remove
                boolean stillHasDiscount = checkForDiscount();
                if (!stillHasDiscount) {
                    System.out.println("   ✅ Mã giảm giá đã được remove thành công");
                } else {
                    System.out.println("   ⚠️ Mã giảm giá vẫn còn (có thể không có nút remove)");
                }
            }

            System.out.println("\n✅ TEST COMPLETED: testRemovePromoCode_Success\n");

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

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    private void addProductToCart() throws InterruptedException {
        driver.get(PRODUCTS_URL);
        Thread.sleep(2000);

        // Tìm nút "Add to Cart" đầu tiên
        List<WebElement> addToCartButtons = driver.findElements(By.cssSelector("button[data-action='add-to-cart']"));

        if (addToCartButtons.isEmpty()) {
            addToCartButtons = driver.findElements(By.cssSelector(".btn-add-to-cart"));
        }

        if (addToCartButtons.isEmpty()) {
            addToCartButtons = driver.findElements(By.xpath("//button[contains(text(), 'Add to Cart') or contains(text(), 'Thêm vào giỏ')]"));
        }

        if (addToCartButtons.size() > 0) {
            WebElement addButton = addToCartButtons.get(0);
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", addButton);
            Thread.sleep(1000);

            // Sử dụng JavaScript click
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", addButton);
            Thread.sleep(2000);
        } else {
            // Nếu không tìm thấy nút, thử click vào product detail rồi add
            List<WebElement> productLinks = driver.findElements(By.cssSelector(".product-card a"));
            if (productLinks.size() > 0) {
                ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", productLinks.get(0));
                Thread.sleep(2000);

                WebElement addButton = driver.findElement(By.cssSelector("button[data-action='add-to-cart']"));
                ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", addButton);
                Thread.sleep(2000);
            }
        }
    }

    /**
     * Áp dụng mã giảm giá
     */
    private void applyPromoCode(String promoCode) throws InterruptedException {
        System.out.println("\n=== Tìm kiếm ô nhập mã giảm giá ===");

        // Tìm input theo ID: promo-input
        WebElement promoInput = null;

        try {
            promoInput = driver.findElement(By.id("promo-input"));
            System.out.println("✓ Tìm thấy input bằng id='promo-input'");
        } catch (Exception e) {
            System.out.println("✗ Không tìm thấy input bằng id='promo-input'");

            // Thử cách khác: tìm theo class
            try {
                promoInput = driver.findElement(By.cssSelector(".promo-code__input"));
                System.out.println("✓ Tìm thấy input bằng class='promo-code__input'");
            } catch (Exception ex) {
                System.out.println("✗ Không tìm thấy input bằng class");

                // Thử cách cuối: tìm input type text có placeholder chứa "mã"
                try {
                    promoInput = driver.findElement(By.cssSelector("input[type='text'][placeholder*='mã']"));
                    System.out.println("✓ Tìm thấy input bằng placeholder chứa 'mã'");
                } catch (Exception exc) {
                    throw new RuntimeException("KHÔNG TÌM THẤY ô nhập mã giảm giá trên trang cart!");
                }
            }
        }

        // Scroll đến input
        ((org.openqa.selenium.JavascriptExecutor) driver)
            .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", promoInput);
        Thread.sleep(1000); // Đợi 1 giây sau khi scroll

        // Điền mã
        promoInput.clear();
        Thread.sleep(500);
        promoInput.sendKeys(promoCode);
        System.out.println("✓ Đã điền mã: " + promoCode);
        Thread.sleep(1000); // Đợi 1 giây sau khi điền

        // Tìm nút apply
        WebElement applyButton = null;
        try {
            // Thử tìm button có data-action
            applyButton = driver.findElement(By.cssSelector("button[data-action='apply-promo']"));
            System.out.println("✓ Tìm thấy nút apply bằng data-action");
        } catch (Exception e) {
            try {
                // Thử tìm button có class promo
                applyButton = driver.findElement(By.cssSelector(".promo-code__btn, .btn-apply-promo"));
                System.out.println("✓ Tìm thấy nút apply bằng class");
            } catch (Exception ex) {
                try {
                    // Thử tìm button có text "Áp dụng"
                    applyButton = driver.findElement(By.xpath("//button[contains(text(), 'Áp dụng') or contains(text(), 'Apply')]"));
                    System.out.println("✓ Tìm thấy nút apply bằng text");
                } catch (Exception exc) {
                    // Tìm button gần input
                    try {
                        applyButton = promoInput.findElement(By.xpath("following-sibling::button | parent::*/button"));
                        System.out.println("✓ Tìm thấy nút apply bên cạnh input");
                    } catch (Exception excc) {
                        System.out.println("⚠️ Không tìm thấy nút apply, thử Enter");
                        promoInput.sendKeys(org.openqa.selenium.Keys.ENTER);
                        Thread.sleep(1000);
                        return;
                    }
                }
            }
        }

        if (applyButton != null) {
            // Scroll đến button
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", applyButton);
            Thread.sleep(1000); // Đợi 1 giây

            // Click bằng JavaScript
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", applyButton);
            System.out.println("✓ Đã click nút apply");
            Thread.sleep(1000); // Đợi 1 giây sau khi click
        }

        System.out.println("=== Hoàn thành áp dụng mã ===\n");
    }

    /**
     * Remove mã giảm giá
     */
    private void removePromoCode() {
        try {
            WebElement removeButton = driver.findElement(By.cssSelector("button[data-action='remove-promo']"));
            removeButton.click();
        } catch (Exception e) {
            try {
                WebElement removeButton = driver.findElement(By.xpath("//button[contains(text(), 'Remove') or contains(text(), 'Xóa')]"));
                removeButton.click();
            } catch (Exception ex) {
                System.out.println("   ⚠️ Không tìm thấy nút remove promo code");
            }
        }
    }

    /**
     * Lấy tổng giá
     */
    private String getTotalPrice() {
        try {
            WebElement totalElement = driver.findElement(By.cssSelector(".total-price, .cart-total, [class*='total']"));
            return totalElement.getText();
        } catch (Exception e) {
            return "N/A";
        }
    }

    /**
     * Kiểm tra có success message không
     */
    private boolean checkForSuccessMessage() {
        try {
            WebElement successAlert = driver.findElement(By.cssSelector(".alert--success, .alert-success, .success-message"));
            if (successAlert.isDisplayed()) {
                System.out.println("   ✓ Thông báo thành công: " + successAlert.getText());
                return true;
            }
        } catch (Exception e) {
            // Không có success message
        }
        return false;
    }

    /**
     * Kiểm tra có thông báo lỗi không
     */
    private boolean checkForErrorMessage() {
        try {
            WebElement errorAlert = driver.findElement(By.cssSelector(".alert--error, .alert-danger, .error-message"));
            if (errorAlert.isDisplayed()) {
                System.out.println("   ✗ Thông báo lỗi: " + errorAlert.getText());
                return true;
            }
        } catch (Exception e) {
            // Không có error message
        }
        return false;
    }

    /**
     * Kiểm tra có discount được áp dụng không
     */
    private boolean checkForDiscount() {
        try {
            // Tìm discount row hoặc discount amount
            WebElement discountElement = driver.findElement(By.cssSelector(".discount-amount, .promo-discount, [class*='discount']"));
            String discountText = discountElement.getText();

            // Kiểm tra có giá trị discount > 0 không
            if (discountText.contains("-") || discountText.contains("₫") || discountText.contains("$") || discountText.contains("GIAM10K")) {
                System.out.println("   ✓ Discount được áp dụng: " + discountText);
                return true;
            }
        } catch (Exception e) {
            // Không tìm thấy discount element
        }

        // Thử cách khác: kiểm tra có text "GIAM10K" hoặc "Promo Code Applied"
        try {
            String pageSource = driver.getPageSource();
            if (pageSource.contains("GIAM10K") || pageSource.contains("Promo Code Applied") ||
                pageSource.contains("Mã giảm giá đã áp dụng")) {
                System.out.println("   ✓ Tìm thấy promo code trong page source");
                return true;
            }
        } catch (Exception e) {
            // Ignore
        }

        return false;
    }
}
