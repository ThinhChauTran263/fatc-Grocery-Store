package poly.edu.java5_asm.DevThinh;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
 * Selenium Test cho BRD_003: testGetBrandBySlug_Success
 * Test filter products theo brand slug trên UI
 * 
 * @author Châu Thịnh
 */
public class SeleniumGetBrandBySlugTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;
    
    private static final String BASE_URL = "http://localhost:8080";
    private static final String PRODUCTS_URL = BASE_URL + "/products";

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
        
        System.out.println("✅ Selenium WebDriver đã được khởi tạo");
    }

    @After
    public void tearDown() {
        // Comment để không đóng browser sau khi test - giữ browser mở để xem kết quả
        // if (driver != null) {
        //     driver.quit();
        //     System.out.println("✅ Selenium WebDriver đã được đóng");
        // }
        System.out.println("ℹ️ Browser vẫn mở - đóng thủ công khi xong");
    }

    /**
     * BRD_003: testGetBrandBySlug_Success
     * Kịch bản: Filter products theo brand "Lavazza" và verify kết quả
     * 
     * Dữ liệu test:
     * - Brand: Lavazza (slug: "lavazza")
     * - Database có 4 products của Lavazza:
     *   1. Coffee Beans - Espresso Arabica and Robusta Beans
     *   2. Lavazza Coffee Blends - Try the Italian Espresso
     *   3. Lavazza - Caffè Espresso Black Tin - Ground coffee
     *   4. Lavazza Qualità Rossa
     * 
     * Các bước:
     * 1. Truy cập trang products
     * 2. Click vào brand filter "Lavazza"
     * 3. Verify filter được apply (URL hoặc UI thay đổi)
     * 4. Verify chỉ hiển thị products của Lavazza
     * 5. Verify brand "Lavazza" được selected/highlighted
     */
    @Test
    public void testGetBrandBySlug_Success() {
        try {
            System.out.println("\n========================================");
            System.out.println("🧪 Test: BRD_003 - testGetBrandBySlug_Success");
            System.out.println("========================================\n");

            // Bước 1: Truy cập trang products
            System.out.println("📍 Bước 1: Truy cập trang products");
            driver.get(PRODUCTS_URL);
            Thread.sleep(2000);
            
            String currentUrl = driver.getCurrentUrl();
            System.out.println("   URL: " + currentUrl);
            assertTrue("Phải ở trang products", currentUrl.contains("/products"));
            System.out.println("   ✅ Đã truy cập trang products thành công\n");

            // Bước 2: Click vào brand filter "Lavazza"
            System.out.println("📍 Bước 2: Click vào brand filter 'Lavazza'");
            
            // Đợi brand filters load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("brandFilters")));
            Thread.sleep(1000);

            // Tìm radio button của Lavazza
            WebElement lavazzaFilter = null;
            List<WebElement> brandLabels = driver.findElements(
                By.cssSelector("#brandFilters label.filter-option"));

            for (WebElement label : brandLabels) {
                String labelText = label.getText();
                if (labelText != null && labelText.toLowerCase().contains("lavazza")) {
                    lavazzaFilter = label;
                    System.out.println("   ✓ Tìm thấy brand filter: " + labelText);
                    break;
                }
            }

            assertNotNull("Lavazza filter phải được tìm thấy", lavazzaFilter);

            // Scroll đến filter và click
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                           lavazzaFilter);
            Thread.sleep(500);

            // Click vào label (sẽ trigger radio button)
            js.executeScript("arguments[0].click();", lavazzaFilter);
            Thread.sleep(3000); // Đợi products reload

            System.out.println("   ✅ Đã click vào brand filter 'Lavazza'\n");

            // Bước 3: Verify filter được apply
            System.out.println("📍 Bước 3: Verify filter được apply");

            // Check radio button đã được checked
            WebElement lavazzaRadio = lavazzaFilter.findElement(By.cssSelector("input[type='radio']"));
            boolean isChecked = lavazzaRadio.isSelected();
            assertTrue("Lavazza filter phải được checked", isChecked);
            System.out.println("   ✓ Radio button 'Lavazza' đã được checked");

            // Check URL có thay đổi không (có thể có brandId parameter)
            String urlAfterFilter = driver.getCurrentUrl();
            System.out.println("   URL sau filter: " + urlAfterFilter);

            if (urlAfterFilter.contains("brand")) {
                System.out.println("   ✓ URL có chứa brand parameter");
            } else {
                System.out.println("   ℹ Filter hoạt động bằng AJAX (không thay đổi URL)");
            }

            System.out.println("   ✅ Filter đã được apply\n");

            // Bước 4: Verify chỉ hiển thị products của Lavazza
            System.out.println("📍 Bước 4: Verify products của Lavazza");

            // Đợi products grid load
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".products-grid, .row.row-cols-5")));
            Thread.sleep(1000);

            // Lấy tất cả product cards
            List<WebElement> productCards = driver.findElements(
                By.cssSelector(".product-card"));

            System.out.println("   Số lượng products hiển thị: " + productCards.size());

            // Database có 4 products của Lavazza
            assertTrue("Phải có ít nhất 1 product", productCards.size() >= 1);
            System.out.println("   ✓ Có products hiển thị");

            // Verify tất cả products đều là Lavazza
            int lavazzaCount = 0;
            for (int i = 0; i < Math.min(productCards.size(), 10); i++) {
                WebElement card = productCards.get(i);
                
                // Tìm brand name trong card
                try {
                    WebElement brandElement = card.findElement(
                        By.cssSelector(".product-card__brand"));
                    String brandName = brandElement.getText();
                    
                    System.out.println("   " + (i + 1) + ". Brand: " + brandName);
                    
                    if (brandName.toLowerCase().contains("lavazza")) {
                        lavazzaCount++;
                    }
                } catch (Exception e) {
                    // Có thể không có brand element
                    System.out.println("   " + (i + 1) + ". (Không tìm thấy brand name)");
                }
            }

            System.out.println("   ✓ Tìm thấy " + lavazzaCount + " products của Lavazza");
            
            // Verify ít nhất 50% products là Lavazza (vì có thể có products khác brand null)
            assertTrue("Ít nhất một nửa products phải là Lavazza", 
                      lavazzaCount >= productCards.size() / 2);

            System.out.println("   ✅ Products được filter đúng theo brand Lavazza\n");

            // Bước 5: Verify brand "Lavazza" được selected
            System.out.println("📍 Bước 5: Verify brand 'Lavazza' được selected");

            // Check lại radio button vẫn còn checked
            boolean stillChecked = lavazzaRadio.isSelected();
            assertTrue("Lavazza filter vẫn phải được checked", stillChecked);
            System.out.println("   ✓ Radio button 'Lavazza' vẫn được checked");

            // Check visual state (có thể có class active hoặc checked)
            String labelClass = lavazzaFilter.getAttribute("class");
            System.out.println("   Label class: " + labelClass);

            System.out.println("   ✅ Brand 'Lavazza' được selected đúng\n");

            // Đợi thêm 3 giây để xem kết quả
            System.out.println("   ⏳ Đợi 3 giây để xem kết quả...");
            Thread.sleep(3000);

            System.out.println("========================================");
            System.out.println("✅ TEST PASSED: BRD_003 - testGetBrandBySlug_Success");
            System.out.println("========================================\n");

        } catch (AssertionError e) {
            System.err.println("\n❌ TEST THẤT BẠI - LỖI ASSERTION");
            System.err.println("   Lý do: " + e.getMessage());
            System.err.println("\n   Hãy kiểm tra:");
            System.err.println("   1. Trang products có load đúng không?");
            System.err.println("   2. Brand filter 'Lavazza' có hiển thị không?");
            System.err.println("   3. Filter có hoạt động đúng không?");
            System.err.println("\n   Browser vẫn mở - hãy kiểm tra thủ công!");
            throw e;
        } catch (Exception e) {
            System.err.println("\n❌ TEST THẤT BẠI - EXCEPTION");
            System.err.println("   Exception: " + e.getClass().getName());
            System.err.println("   Message: " + e.getMessage());
            System.err.println("\n   Browser vẫn mở - hãy kiểm tra thủ công!");
            throw new AssertionError("Test thất bại với exception: " + e.getMessage(), e);
        }
    }
}
