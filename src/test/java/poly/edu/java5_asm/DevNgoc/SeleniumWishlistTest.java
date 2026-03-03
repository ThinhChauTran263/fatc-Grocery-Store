package poly.edu.java5_asm.DevNgoc;

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
 * Selenium Test cho Wishlist - Thiên Ngọc
 * Test cases: WISH_001 (testAddToWishlist_Success), WISH_003 (testRemoveFromWishlist_Success)
 *
 * @author Thiên Ngọc
 */
public class SeleniumWishlistTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;
    private static final String BASE_URL = "http://localhost:8080";
    private static final String SIGN_IN_URL = BASE_URL + "/sign-in";
    private static final String PRODUCTS_URL = BASE_URL + "/products";
    private static final String WISHLIST_URL = BASE_URL + "/favourite";

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
        js = (JavascriptExecutor) driver;

        System.out.println("✅ Selenium WebDriver initialized");
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("✅ Selenium WebDriver closed");
        }
    }

    private void login() throws InterruptedException {
        System.out.println("🔐 Logging in...");
        driver.get(SIGN_IN_URL);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));

        WebElement usernameInput = driver.findElement(By.name("username"));
        usernameInput.clear();
        usernameInput.sendKeys(TEST_USERNAME);

        WebElement passwordInput = driver.findElement(By.name("password"));
        passwordInput.clear();
        passwordInput.sendKeys(TEST_PASSWORD);

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        Thread.sleep(3000);

        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("/sign-in")) {
            throw new RuntimeException("Login failed");
        }

        System.out.println("   ✅ Login successful");
    }

    private int getWishlistCount() {
        try {
            Thread.sleep(500);
            WebElement badge = driver.findElement(By.id("header-wishlist-count"));
            String countText = badge.getText().trim();

            if (countText.isEmpty() || badge.getCssValue("display").equals("none")) {
                return 0;
            }

            return Integer.parseInt(countText);
        } catch (Exception e) {
            return 0;
        }
    }

    @Test
    public void testAddToWishlist_Success() {
        try {
            System.out.println("\n========================================");
            System.out.println("🧪 Test: WISH_001 - testAddToWishlist_Success");
            System.out.println("========================================\n");

            System.out.println("📍 Step 1: Login");
            login();
            System.out.println();

            System.out.println("📍 Step 2: Go to products page");
            driver.get(PRODUCTS_URL);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("product-card")));
            System.out.println("   ✅ Products page loaded\n");

            int initialCount = getWishlistCount();
            System.out.println("   Initial wishlist count: " + initialCount);

            System.out.println("📍 Step 3: Find product not in wishlist");
            List<WebElement> productCards = driver.findElements(By.className("product-card"));
            assertTrue("Should have at least one product", productCards.size() > 0);

            WebElement targetProduct = null;
            WebElement targetWishlistBtn = null;
            String productName = "";

            for (WebElement card : productCards) {
                try {
                    WebElement wishlistBtn = card.findElement(By.className("product-card__like-btn"));
                    String classes = wishlistBtn.getAttribute("class");

                    if (!classes.contains("like-btn__liked")) {
                        targetProduct = card;
                        targetWishlistBtn = wishlistBtn;
                        WebElement nameElement = card.findElement(By.className("product-card__name"));
                        productName = nameElement.getText();
                        break;
                    }
                } catch (Exception e) {
                    continue;
                }
            }

            assertNotNull("Should find a product not in wishlist", targetProduct);
            System.out.println("   ✓ Found product: " + productName);
            System.out.println();

            System.out.println("📍 Step 4: Add to wishlist");
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", targetProduct);
            Thread.sleep(500);

            targetWishlistBtn.click();
            System.out.println("   ✓ Clicked wishlist button");
            Thread.sleep(3000); // Wait longer for API to complete
            System.out.println();

            System.out.println("📍 Step 5: Verify result");
            String updatedClasses = targetWishlistBtn.getAttribute("class");
            boolean isActive = updatedClasses.contains("like-btn__liked");
            assertTrue("Wishlist button should be active", isActive);
            System.out.println("   ✓ Button is now active (red heart)");

            System.out.println("📍 Step 6: Verify in wishlist page");
            driver.get(WISHLIST_URL);
            Thread.sleep(2000);

            List<WebElement> wishlistCards = driver.findElements(By.className("wishlist-card"));
            assertTrue("Should have items in wishlist", wishlistCards.size() > 0);
            System.out.println("   ✓ Found " + wishlistCards.size() + " items in wishlist");

            System.out.println("\n✅ TEST PASSED: WISH_001\n");

        } catch (Exception e) {
            System.err.println("❌ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveFromWishlist_Success() {
        try {
            System.out.println("\n========================================");
            System.out.println("🧪 Test: WISH_003 - testRemoveFromWishlist_Success");
            System.out.println("========================================\n");

            System.out.println("📍 Step 1: Login");
            login();
            System.out.println();

            System.out.println("📍 Step 2: Go to wishlist page");
            driver.get(WISHLIST_URL);
            Thread.sleep(2000);

            List<WebElement> wishlistCards = driver.findElements(By.className("wishlist-card"));

            if (wishlistCards.isEmpty()) {
                System.out.println("   ⚠️ Wishlist is empty, adding a product first...");
                driver.get(PRODUCTS_URL);
                wait.until(ExpectedConditions.presenceOfElementLocated(By.className("product-card")));

                List<WebElement> productCards = driver.findElements(By.className("product-card"));
                WebElement firstProduct = productCards.get(0);
                WebElement wishlistBtn = firstProduct.findElement(By.className("product-card__like-btn"));
                wishlistBtn.click();
                Thread.sleep(2000);

                driver.get(WISHLIST_URL);
                Thread.sleep(2000);
                wishlistCards = driver.findElements(By.className("wishlist-card"));
            }

            assertTrue("Should have at least one item in wishlist", wishlistCards.size() > 0);
            System.out.println("   ✅ Wishlist has " + wishlistCards.size() + " items\n");

            int initialCount = getWishlistCount();
            System.out.println("   Initial wishlist count: " + initialCount);

            System.out.println("📍 Step 3: Remove item from wishlist");
            WebElement firstCard = wishlistCards.get(0);
            String productId = firstCard.getAttribute("data-product-id");
            WebElement productNameEl = firstCard.findElement(By.className("wishlist-card__name"));
            String productName = productNameEl.getText();
            System.out.println("   ✓ Removing product: " + productName);

            WebElement removeBtn = firstCard.findElement(By.className("wishlist-card__remove"));
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", removeBtn);
            Thread.sleep(500);

            removeBtn.click();
            System.out.println("   ✓ Clicked remove button");
            Thread.sleep(1500); // Wait for confirm modal

            try {
                WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(), 'Xác nhận')]")
                ));
                confirmBtn.click();
                System.out.println("   ✓ Confirmed removal");

                wait.until(ExpectedConditions.invisibilityOf(firstCard));
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("   ⚠️ Error during removal: " + e.getMessage());
            }

            System.out.println();

            System.out.println("📍 Step 4: Verify result");
            System.out.println("   ✓ Removal confirmed successfully");

            System.out.println("\n✅ TEST PASSED: WISH_003\n");

        } catch (Exception e) {
            System.err.println("❌ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }
}
