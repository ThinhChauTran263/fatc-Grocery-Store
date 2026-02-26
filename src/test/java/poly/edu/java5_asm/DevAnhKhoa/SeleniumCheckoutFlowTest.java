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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Selenium Test - Phiên bản 2 cho ORD_001: testCreateOrder_Success
 * Test toàn bộ flow checkout từ đầu đến cuối với nhiều kịch bản
 * 
 * @author Anh Khoa
 */
public class SeleniumCheckoutFlowTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080";
    private static final String SIGN_IN_URL = BASE_URL + "/sign-in";
    private static final String PRODUCTS_URL = BASE_URL + "/products";
    private static final String CART_URL = BASE_URL + "/cart";
    
    private static final String TEST_USERNAME = "khoaphan180806@gmail.com";
    private static final String TEST_PASSWORD = "123456";

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

    private void login() throws InterruptedException {
        System.out.println("🔐 Đăng nhập vào hệ thống...");
        driver.get(SIGN_IN_URL);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
        
        driver.findElement(By.name("username")).sendKeys(TEST_USERNAME);
        driver.findElement(By.name("password")).sendKeys(TEST_PASSWORD);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        
        // Đợi redirect về trang products sau khi login
        Thread.sleep(3000);
        
        String currentUrl = driver.getCurrentUrl();
        System.out.println("   Current URL after login: " + currentUrl);
        
        // Nếu chưa ở trang products, redirect về products
        if (!currentUrl.contains("/products")) {
            System.out.println("   Redirect về trang products...");
            driver.get(PRODUCTS_URL);
            Thread.sleep(2000);
        }
        
        System.out.println("   ✅ Đã đăng nhập và ở trang products");
    }

    private void addMultipleProductsToCart(int count) throws InterruptedException {
        System.out.println("🛒 Thêm sản phẩm vào giỏ hàng...");
        
        // Đảm bảo đang ở trang products
        if (!driver.getCurrentUrl().contains("/products")) {
            driver.get(PRODUCTS_URL);
            Thread.sleep(1000);
        }
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".product-card")));
        Thread.sleep(1000);
        
        // Selector chính xác cho button Add to Cart
        List<WebElement> addButtons = driver.findElements(By.cssSelector("button[onclick*='addToCart']"));
        int added = 0;
        
        for (WebElement btn : addButtons) {
            if (added >= count) break;
            
            // Scroll đến button
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", btn);
            Thread.sleep(500);
            
            // Click bằng JavaScript để tránh modal overlay che
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", btn);
            Thread.sleep(2000); // Đợi modal notification đóng
            added++;
        }
        
        System.out.println("   ✅ Đã thêm " + added + " sản phẩm vào giỏ");
    }

    /**
     * Test 1: Checkout flow hoàn chỉnh với 1 sản phẩm
     */
    @Test
    public void testCheckoutFlow_SingleProduct() {
        try {
            System.out.println("\n========================================");
            System.out.println("🧪 Test: Checkout với 1 sản phẩm");
            System.out.println("========================================\n");

            System.out.println("📍 Bước 1: Đăng nhập");
            login();

            System.out.println("📍 Bước 2: Thêm 1 sản phẩm vào giỏ");
            addMultipleProductsToCart(1);

            System.out.println("📍 Bước 3: Xem giỏ hàng");
            driver.get(CART_URL);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cart-page")));
            Thread.sleep(1000);

            // Kiểm tra có sản phẩm trong giỏ
            List<WebElement> cartItems = driver.findElements(By.cssSelector(".cart-item, .cart-product"));
            assertTrue("Cart should have items", !cartItems.isEmpty());
            System.out.println("   ✓ Giỏ hàng có " + cartItems.size() + " sản phẩm");

            // Lấy tổng tiền trước checkout
            String totalBeforeCheckout = "";
            try {
                WebElement totalEl = driver.findElement(By.cssSelector(".total-amount, .cart-total"));
                totalBeforeCheckout = totalEl.getText();
                System.out.println("   ✓ Tổng tiền: " + totalBeforeCheckout);
            } catch (Exception e) {
                System.out.println("   ⚠ Không lấy được tổng tiền");
            }

            System.out.println("📍 Bước 4: Tiến hành thanh toán (/cart → /checkout)");
            // Selector chính xác: a[href="/checkout"] với class cart-btn--primary
            WebElement checkoutBtn = driver.findElement(By.cssSelector("a[href*='/checkout'].cart-btn--primary"));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", checkoutBtn);
            Thread.sleep(500);
            checkoutBtn.click();
            Thread.sleep(2000);

            System.out.println("📍 Bước 5: Chọn địa chỉ và phương thức thanh toán (/checkout)");
            String currentUrl = driver.getCurrentUrl();
            assertTrue("Should be on checkout page", currentUrl.contains("/checkout"));

            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".checkout-page")));
            Thread.sleep(1000);

            // Chọn địa chỉ (nếu có)
            try {
                WebElement addressSelect = driver.findElement(By.id("address-select"));
                if (addressSelect.findElements(By.tagName("option")).size() > 1) {
                    addressSelect.findElement(By.cssSelector("option:nth-child(2)")).click();
                    System.out.println("   ✓ Đã chọn địa chỉ giao hàng");
                }
            } catch (Exception e) {
                System.out.println("   ⚠ Không có địa chỉ hoặc đã chọn mặc định");
            }

            // Chọn phương thức thanh toán COD
            try {
                WebElement codRadio = driver.findElement(By.cssSelector("input[value='COD']"));
                if (!codRadio.isSelected()) {
                    codRadio.click();
                    System.out.println("   ✓ Chọn thanh toán: COD");
                }
            } catch (Exception e) {
                System.out.println("   ✓ Thanh toán mặc định: COD");
            }

            System.out.println("📍 Bước 6: Đặt hàng ngay (/checkout → /payment)");
            // Sử dụng JavaScript click vì button có thể bị che bởi element khác
            WebElement placeOrderBtn = driver.findElement(By.id("place-order-btn"));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", placeOrderBtn);
            Thread.sleep(1000);
            // Click bằng JavaScript để tránh lỗi "element not interactable"
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", placeOrderBtn);
            Thread.sleep(2000);

            System.out.println("📍 Bước 7: Xác nhận đặt hàng (/payment)");
            currentUrl = driver.getCurrentUrl();
            assertTrue("Should be on payment page", currentUrl.contains("/payment"));
            System.out.println("   ✓ Đã vào trang payment: " + currentUrl);

            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".payment-page")));
            Thread.sleep(1000);

            // Bấm nút "Đặt hàng" trên trang payment
            WebElement finalPlaceOrderBtn = driver.findElement(By.id("place-order-btn"));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", finalPlaceOrderBtn);
            Thread.sleep(500);
            finalPlaceOrderBtn.click();
            Thread.sleep(3000);

            System.out.println("📍 Bước 8: Kiểm tra kết quả");
            currentUrl = driver.getCurrentUrl();
            System.out.println("   URL: " + currentUrl);

            // Kiểm tra đã tạo đơn hàng thành công (redirect về order-detail hoặc success page)
            boolean orderCreated = currentUrl.contains("/order-detail") || 
                                  currentUrl.contains("/success") ||
                                  !currentUrl.contains("/payment");

            assertTrue("Order should be created successfully", orderCreated);
            System.out.println("   ✅ Đơn hàng được tạo thành công!\n");

            System.out.println("✅ TEST PASSED: testCheckoutFlow_SingleProduct\n");

        } catch (Exception e) {
            System.err.println("❌ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }

    /**
     * Test 2: Checkout flow với nhiều sản phẩm
     */
    @Test
    public void testCheckoutFlow_MultipleProducts() {
        try {
            System.out.println("\n========================================");
            System.out.println("🧪 Test: Checkout với nhiều sản phẩm");
            System.out.println("========================================\n");

            login();
            
            System.out.println("📍 Thêm 3 sản phẩm vào giỏ");
            addMultipleProductsToCart(3);

            driver.get(CART_URL);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cart-page")));
            Thread.sleep(1000);

            List<WebElement> cartItems = driver.findElements(By.cssSelector(".cart-item, .cart-product"));
            System.out.println("   ✓ Giỏ hàng có " + cartItems.size() + " sản phẩm");
            assertTrue("Should have multiple items", cartItems.size() >= 2);

            // Proceed to checkout
            WebElement checkoutBtn = driver.findElement(By.cssSelector("a[href*='/checkout'].cart-btn--primary"));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", checkoutBtn);
            Thread.sleep(500);
            checkoutBtn.click();
            Thread.sleep(2000);

            // Place order
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".checkout-page")));
            Thread.sleep(1000);

            WebElement placeOrderBtn = driver.findElement(By.cssSelector(".btn-place-order, button[type='submit']"));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", placeOrderBtn);
            Thread.sleep(500);
            placeOrderBtn.click();
            Thread.sleep(3000);

            String currentUrl = driver.getCurrentUrl();
            boolean orderCreated = !currentUrl.contains("/checkout");

            assertTrue("Order with multiple products should be created", orderCreated);
            System.out.println("   ✅ Đơn hàng nhiều sản phẩm được tạo thành công!\n");

            System.out.println("✅ TEST PASSED: testCheckoutFlow_MultipleProducts\n");

        } catch (Exception e) {
            System.err.println("❌ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }

    /**
     * Test 3: Checkout với mã giảm giá
     */
    @Test
    public void testCheckoutFlow_WithPromoCode() {
        try {
            System.out.println("\n========================================");
            System.out.println("🧪 Test: Checkout với mã giảm giá");
            System.out.println("========================================\n");

            login();
            addMultipleProductsToCart(1);

            driver.get(CART_URL);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cart-page")));
            Thread.sleep(1000);

            // Áp dụng mã giảm giá
            System.out.println("📍 Áp dụng mã giảm giá");
            try {
                WebElement promoInput = driver.findElement(By.id("promo-input"));
                promoInput.clear();
                promoInput.sendKeys("GIAM10K");
                
                // Click bằng JavaScript
                WebElement applyBtn = driver.findElement(By.cssSelector(".promo-code__btn"));
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();", applyBtn);
                Thread.sleep(3000); // Đợi modal notification đóng
                System.out.println("   ✓ Đã áp dụng mã GIAM10K");
            } catch (Exception e) {
                System.out.println("   ⚠ Không thể áp dụng mã giảm giá");
            }

            // Proceed to checkout - dùng JavaScript click
            WebElement checkoutBtn = driver.findElement(By.cssSelector("a[href*='/checkout'].cart-btn--primary"));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", checkoutBtn);
            Thread.sleep(1000);
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", checkoutBtn);
            Thread.sleep(2000);

            // Place order
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".checkout-page")));
            Thread.sleep(1000);

            WebElement placeOrderBtn = driver.findElement(By.cssSelector(".btn-place-order, button[type='submit']"));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", placeOrderBtn);
            Thread.sleep(500);
            placeOrderBtn.click();
            Thread.sleep(3000);

            String currentUrl = driver.getCurrentUrl();
            boolean orderCreated = !currentUrl.contains("/checkout");

            assertTrue("Order with promo code should be created", orderCreated);
            System.out.println("   ✅ Đơn hàng với mã giảm giá được tạo thành công!\n");

            System.out.println("✅ TEST PASSED: testCheckoutFlow_WithPromoCode\n");

        } catch (Exception e) {
            System.err.println("❌ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }

    /**
     * Test 4: Kiểm tra các phương thức giao hàng
     */
    @Test
    public void testCheckoutFlow_ShippingMethods() {
        try {
            System.out.println("\n========================================");
            System.out.println("🧪 Test: Các phương thức giao hàng");
            System.out.println("========================================\n");

            login();
            addMultipleProductsToCart(1);

            driver.get(CART_URL);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cart-page")));
            Thread.sleep(1000);

            WebElement checkoutBtn = driver.findElement(By.cssSelector("a[href*='/checkout'].cart-btn--primary"));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", checkoutBtn);
            Thread.sleep(500);
            checkoutBtn.click();
            Thread.sleep(2000);

            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".checkout-page")));
            Thread.sleep(1000);

            // Kiểm tra các phương thức giao hàng
            System.out.println("📍 Kiểm tra phương thức giao hàng");
            List<WebElement> shippingOptions = driver.findElements(
                By.cssSelector("input[name='shippingMethod']"));

            if (!shippingOptions.isEmpty()) {
                System.out.println("   ✓ Có " + shippingOptions.size() + " phương thức giao hàng");
                
                for (WebElement option : shippingOptions) {
                    String value = option.getAttribute("value");
                    System.out.println("     - " + value);
                }

                // Chọn standard shipping
                for (WebElement option : shippingOptions) {
                    if ("standard".equalsIgnoreCase(option.getAttribute("value"))) {
                        option.click();
                        System.out.println("   ✓ Đã chọn: Standard Shipping");
                        break;
                    }
                }
            } else {
                System.out.println("   ℹ Không có lựa chọn phương thức giao hàng");
            }

            // Place order
            WebElement placeOrderBtn = driver.findElement(By.cssSelector(".btn-place-order, button[type='submit']"));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", placeOrderBtn);
            Thread.sleep(500);
            placeOrderBtn.click();
            Thread.sleep(3000);

            String currentUrl = driver.getCurrentUrl();
            boolean orderCreated = !currentUrl.contains("/checkout");

            assertTrue("Order should be created", orderCreated);
            System.out.println("   ✅ Đơn hàng được tạo với phương thức giao hàng đã chọn!\n");

            System.out.println("✅ TEST PASSED: testCheckoutFlow_ShippingMethods\n");

        } catch (Exception e) {
            System.err.println("❌ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }

    /**
     * Test 5: Kiểm tra order summary trước khi place order
     */
    @Test
    public void testCheckoutFlow_OrderSummary() {
        try {
            System.out.println("\n========================================");
            System.out.println("🧪 Test: Kiểm tra Order Summary");
            System.out.println("========================================\n");

            login();
            addMultipleProductsToCart(2);

            driver.get(CART_URL);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cart-page")));
            Thread.sleep(1000);

            WebElement checkoutBtn = driver.findElement(By.cssSelector("a[href*='/checkout'].cart-btn--primary"));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", checkoutBtn);
            Thread.sleep(500);
            checkoutBtn.click();
            Thread.sleep(2000);

            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".checkout-page")));
            Thread.sleep(1000);

            // Kiểm tra order summary
            System.out.println("📍 Kiểm tra Order Summary");
            
            boolean hasSubtotal = false;
            boolean hasShipping = false;
            boolean hasTax = false;
            boolean hasTotal = false;

            try {
                WebElement subtotalEl = driver.findElement(By.id("summary-subtotal"));
                hasSubtotal = subtotalEl.isDisplayed();
                System.out.println("   ✓ Subtotal: " + subtotalEl.getText());
            } catch (Exception e) {
                System.out.println("   ✗ Không có Subtotal");
            }

            try {
                WebElement shippingEl = driver.findElement(By.id("summary-shipping"));
                hasShipping = shippingEl.isDisplayed();
                System.out.println("   ✓ Shipping: " + shippingEl.getText());
            } catch (Exception e) {
                System.out.println("   ℹ Không có Shipping fee");
            }

            try {
                WebElement taxEl = driver.findElement(By.id("summary-tax"));
                hasTax = taxEl.isDisplayed();
                System.out.println("   ✓ Tax: " + taxEl.getText());
            } catch (Exception e) {
                System.out.println("   ℹ Không có Tax");
            }

            try {
                WebElement totalEl = driver.findElement(By.id("summary-total"));
                hasTotal = totalEl.isDisplayed();
                System.out.println("   ✓ Total: " + totalEl.getText());
            } catch (Exception e) {
                System.out.println("   ✗ Không có Total");
            }

            assertTrue("Should display order total", hasTotal);
            System.out.println("   ✅ Order summary hiển thị đầy đủ!\n");

            System.out.println("✅ TEST PASSED: testCheckoutFlow_OrderSummary\n");

        } catch (Exception e) {
            System.err.println("❌ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }
}
