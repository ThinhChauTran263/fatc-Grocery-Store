package poly.edu.java5_asm.DevAnhKhoa;

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
 * Selenium Test đơn giản cho CART_017: testApplyPromoCode_Invalid
 * Flow: Đăng nhập -> Vào products -> Thêm sản phẩm -> Vào giỏ hàng -> Áp sai mã -> Hiện lỗi
 * 
 * @author Anh Khoa
 */
public class SeleniumSimplePromoCodeTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080";
    private static final String SIGN_IN_URL = BASE_URL + "/sign-in";
    private static final String PRODUCTS_URL = BASE_URL + "/products";
    private static final String CART_URL = BASE_URL + "/cart";
    
    private static final String TEST_USERNAME = "admin";
    private static final String TEST_PASSWORD = "password123";

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        
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
     * CART_017: testApplyPromoCode_Invalid
     * Flow đơn giản: Login -> Products -> Add to Cart -> Cart -> Apply Invalid Code -> Show Error
     */
    @Test
    public void testApplyInvalidPromoCode_SimpleFlow() {
        try {
            System.out.println("\n========================================");
            System.out.println("🧪 Test: Áp dụng mã giảm giá không hợp lệ");
            System.out.println("========================================\n");

            // Bước 1: Đăng nhập
            System.out.println("📍 Bước 1: Đăng nhập");
            driver.get(SIGN_IN_URL);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
            
            driver.findElement(By.name("username")).sendKeys(TEST_USERNAME);
            driver.findElement(By.name("password")).sendKeys(TEST_PASSWORD);
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            
            // Đợi redirect
            Thread.sleep(3000);
            System.out.println("   ✅ Đã đăng nhập thành công\n");

            // Bước 2: Vào trang products
            System.out.println("📍 Bước 2: Vào trang products");
            String currentUrl = driver.getCurrentUrl();
            System.out.println("   Current URL sau login: " + currentUrl);
            
            // Luôn redirect về products để chắc chắn
            System.out.println("   Redirect về trang products...");
            driver.get(PRODUCTS_URL);
            Thread.sleep(2000);
            
            currentUrl = driver.getCurrentUrl();
            System.out.println("   Current URL: " + currentUrl);
            
            // Đợi trang products load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".product-card, .product-item, .products-container")));
            System.out.println("   ✅ Đã vào trang products\n");

            // Bước 3: Thêm sản phẩm vào giỏ
            System.out.println("📍 Bước 3: Thêm sản phẩm vào giỏ hàng");
            
            // Thử nhiều selector khác nhau cho button Add to Cart
            WebElement addToCartBtn = null;
            String[] selectors = {
                ".btn-add-to-cart",
                "button.add-to-cart",
                ".add-to-cart-btn",
                "button[onclick*='addToCart']",
                "a[href*='cart/add']",
                ".product-card button",
                ".product-item button"
            };
            
            for (String selector : selectors) {
                try {
                    addToCartBtn = driver.findElement(By.cssSelector(selector));
                    System.out.println("   ✓ Tìm thấy button với selector: " + selector);
                    break;
                } catch (Exception e) {
                    System.out.println("   ✗ Không tìm thấy với selector: " + selector);
                }
            }
            
            if (addToCartBtn == null) {
                // In ra HTML để debug
                String pageSource = driver.getPageSource();
                System.out.println("   ⚠ Không tìm thấy button Add to Cart!");
                System.out.println("   📄 Page title: " + driver.getTitle());
                
                // Tìm tất cả buttons
                List<WebElement> allButtons = driver.findElements(By.tagName("button"));
                System.out.println("   📊 Tìm thấy " + allButtons.size() + " buttons trên trang");
                for (int i = 0; i < Math.min(5, allButtons.size()); i++) {
                    WebElement btn = allButtons.get(i);
                    System.out.println("     Button " + (i+1) + ": " + btn.getText() + " | class=" + btn.getAttribute("class"));
                }
                
                fail("Không tìm thấy button Add to Cart. Vui lòng kiểm tra selector hoặc đảm bảo có sản phẩm trên trang.");
            }
            
            // Scroll đến button
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", addToCartBtn);
            Thread.sleep(500);
            
            addToCartBtn.click();
            System.out.println("   ✅ Đã thêm sản phẩm vào giỏ\n");
            Thread.sleep(2000);

            // Bước 4: Vào giỏ hàng
            System.out.println("📍 Bước 4: Vào giỏ hàng");
            driver.get(CART_URL);
            Thread.sleep(2000);
            
            // Đợi trang cart load - selector chính xác là .cart-page
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cart-page")));
            System.out.println("   ✓ Trang cart đã load");
            
            // Đợi cart content hiển thị (có thể là empty hoặc có items)
            Thread.sleep(1000);
            System.out.println("   ✅ Đã vào trang giỏ hàng\n");

            // Bước 5: Áp dụng mã giảm giá không hợp lệ
            System.out.println("📍 Bước 5: Áp dụng mã giảm giá không hợp lệ");
            String invalidPromoCode = "INVALID_CODE_123";
            
            // Selector chính xác: #promo-input (không phải #promo-code-input)
            WebElement promoCodeInput = driver.findElement(By.id("promo-input"));
            promoCodeInput.clear();
            promoCodeInput.sendKeys(invalidPromoCode);
            System.out.println("   ✓ Đã nhập mã: " + invalidPromoCode);
            
            // Button có class .promo-code__btn và onclick="applyPromo()"
            WebElement applyButton = driver.findElement(By.cssSelector(".promo-code__btn"));
            applyButton.click();
            System.out.println("   ✓ Đã nhấn nút 'Áp dụng'\n");
            
            // Đợi modal hiển thị
            Thread.sleep(2000);

            // Bước 6: Kiểm tra hiển thị thông báo lỗi trong modal
            System.out.println("📍 Bước 6: Kiểm tra hiển thị thông báo lỗi");
            
            // Đợi modal hiển thị - NotificationModal với ID #notificationModal
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notificationModal")));
            WebElement modal = driver.findElement(By.id("notificationModal"));
            assertTrue("Modal phải hiển thị", modal.isDisplayed());
            System.out.println("   ✓ Modal thông báo đã hiển thị");
            
            // Kiểm tra icon error (màu đỏ)
            WebElement modalIcon = driver.findElement(By.id("notificationIcon"));
            String iconClass = modalIcon.getAttribute("class");
            assertTrue("Modal phải có icon error", iconClass.contains("notification-modal__icon--error"));
            System.out.println("   ✓ Icon error đã hiển thị");
            
            // Kiểm tra title
            WebElement modalTitle = driver.findElement(By.id("notificationTitle"));
            String titleText = modalTitle.getText();
            assertEquals("Lỗi", titleText);
            System.out.println("   ✓ Title: " + titleText);
            
            // Kiểm tra message
            WebElement modalMessage = driver.findElement(By.id("notificationMessage"));
            String errorMessage = modalMessage.getText();
            System.out.println("   ✓ Thông báo lỗi: " + errorMessage);
            
            // Kiểm tra nội dung error message
            assertTrue("❌ Thông báo lỗi phải chứa từ khóa liên quan", 
                      errorMessage.toLowerCase().contains("không hợp lệ") || 
                      errorMessage.toLowerCase().contains("invalid") ||
                      errorMessage.toLowerCase().contains("không tồn tại") ||
                      errorMessage.toLowerCase().contains("mã giảm giá"));

            System.out.println("   ✅ Hệ thống đã từ chối mã giảm giá không hợp lệ!\n");
            
            // Đóng modal
            WebElement okButton = driver.findElement(By.cssSelector(".notification-modal__btn"));
            okButton.click();
            Thread.sleep(500);

            System.out.println("========================================");
            System.out.println("✅ TEST PASSED: Áp dụng mã giảm giá không hợp lệ");
            System.out.println("========================================\n");

        } catch (Exception e) {
            System.err.println("❌ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    /**
     * Test bổ sung: Áp dụng mã hợp lệ để so sánh
     */
    @Test
    public void testApplyValidPromoCode_SimpleFlow() {
        try {
            System.out.println("\n========================================");
            System.out.println("🧪 Test: Áp dụng mã giảm giá hợp lệ (để so sánh)");
            System.out.println("========================================\n");

            // Đăng nhập
            System.out.println("📍 Đăng nhập");
            driver.get(SIGN_IN_URL);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
            driver.findElement(By.name("username")).sendKeys(TEST_USERNAME);
            driver.findElement(By.name("password")).sendKeys(TEST_PASSWORD);
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            Thread.sleep(3000);

            // Vào products
            System.out.println("📍 Vào trang products");
            driver.get(PRODUCTS_URL);
            Thread.sleep(2000);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".product-card, .product-item, .products-container")));

            // Thêm sản phẩm
            System.out.println("📍 Thêm sản phẩm");
            WebElement addBtn = null;
            String[] selectors = {
                ".btn-add-to-cart",
                "button.add-to-cart",
                ".add-to-cart-btn",
                "button[onclick*='addToCart']",
                ".product-card button",
                ".product-item button"
            };
            
            for (String selector : selectors) {
                try {
                    addBtn = driver.findElement(By.cssSelector(selector));
                    System.out.println("   ✓ Tìm thấy button: " + selector);
                    break;
                } catch (Exception e) {
                    // Try next selector
                }
            }
            
            if (addBtn == null) {
                System.out.println("   ⚠ Không tìm thấy button Add to Cart");
                List<WebElement> allButtons = driver.findElements(By.tagName("button"));
                System.out.println("   📊 Có " + allButtons.size() + " buttons trên trang");
                fail("Không tìm thấy button Add to Cart");
            }
            
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", addBtn);
            Thread.sleep(500);
            addBtn.click();
            Thread.sleep(2000);

            // Vào giỏ hàng
            System.out.println("📍 Vào giỏ hàng");
            driver.get(CART_URL);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cart-page")));
            Thread.sleep(1000);

            // Áp dụng mã hợp lệ
            System.out.println("📍 Áp dụng mã hợp lệ: GIAM10K");
            String validPromoCode = "GIAM10K";
            WebElement promoInput = driver.findElement(By.id("promo-input"));
            promoInput.clear();
            promoInput.sendKeys(validPromoCode);
            driver.findElement(By.cssSelector(".promo-code__btn")).click();
            Thread.sleep(2000);

            // Kiểm tra kết quả - modal success
            System.out.println("📍 Kiểm tra kết quả");
            
            // Đợi modal success hiển thị
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notificationModal")));
            WebElement modal = driver.findElement(By.id("notificationModal"));
            assertTrue("Modal phải hiển thị", modal.isDisplayed());
            
            // Kiểm tra icon success (màu xanh)
            WebElement modalIcon = driver.findElement(By.id("notificationIcon"));
            String iconClass = modalIcon.getAttribute("class");
            assertTrue("Modal phải có icon success", iconClass.contains("notification-modal__icon--success"));
            System.out.println("   ✓ Icon success đã hiển thị");
            
            // Kiểm tra message
            WebElement modalMessage = driver.findElement(By.id("notificationMessage"));
            String successMessage = modalMessage.getText();
            System.out.println("   ✓ Thông báo: " + successMessage);
            assertTrue("Message phải chứa 'thành công'", 
                      successMessage.toLowerCase().contains("thành công"));

            System.out.println("   ✅ Mã hợp lệ được áp dụng thành công!\n");
            
            // Đóng modal
            driver.findElement(By.cssSelector(".notification-modal__btn")).click();
            Thread.sleep(500);

            System.out.println("✅ TEST PASSED: Áp dụng mã hợp lệ\n");

        } catch (Exception e) {
            System.err.println("❌ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }
}
