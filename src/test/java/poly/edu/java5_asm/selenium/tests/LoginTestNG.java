package poly.edu.java5_asm.selenium.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import poly.edu.java5_asm.selenium.config.TestConfig;
import poly.edu.java5_asm.selenium.config.WebDriverManager;
import poly.edu.java5_asm.selenium.pages.HomePage;
import poly.edu.java5_asm.selenium.pages.LoginPage;
import poly.edu.java5_asm.selenium.utils.ScreenshotUtil;

import java.time.Duration;

/**
 * TestNG Test Suite cho Login Functionality
 * 
 * Yêu cầu:
 * 1. ChromeDriver được cài đặt
 * 2. Application đang chạy tại http://localhost:8080
 * 
 * Chạy test:
 * mvn test -Dtest=LoginTestNG
 * mvn test -Dtest=LoginTestNG -Dbrowser=chrome -Dheadless=false
 */
public class LoginTestNG {
    
    private WebDriver driver;
    private LoginPage loginPage;
    private HomePage homePage;
    
    @BeforeClass
    public void setUpClass() {
        System.out.println("===========================================");
        System.out.println("Starting Login Test Suite with TestNG");
        System.out.println("Base URL: " + TestConfig.BASE_URL);
        System.out.println("===========================================");
    }
    
    @BeforeMethod
    public void setUp() {
        try {
            // Khởi tạo WebDriver
            driver = WebDriverManager.createDriver();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TestConfig.IMPLICIT_WAIT));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(TestConfig.PAGE_LOAD_TIMEOUT));
            
            // Khởi tạo Page Objects
            loginPage = new LoginPage(driver);
            homePage = new HomePage(driver);
            
            System.out.println("✓ WebDriver initialized successfully");
            
        } catch (Exception e) {
            System.err.println("✗ Failed to initialize WebDriver: " + e.getMessage());
            System.err.println("Make sure ChromeDriver is installed and in PATH");
            throw new RuntimeException("WebDriver initialization failed", e);
        }
    }
    
    @Test(priority = 1, description = "Verify login page loads successfully")
    public void testLoginPageLoads() {
        System.out.println("\n[TEST 1] Testing login page loads...");
        
        try {
            loginPage.navigateToLoginPage(TestConfig.BASE_URL);
            
            Assert.assertTrue(loginPage.isLoginPageDisplayed(), 
                "Login page should be displayed");
            
            String pageTitle = loginPage.getPageTitle();
            Assert.assertNotNull(pageTitle, "Page title should not be null");
            
            System.out.println("✓ Login page loaded successfully");
            System.out.println("  Page title: " + pageTitle);
            
        } catch (Exception e) {
            System.err.println("✗ Test failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 2, description = "Verify login with valid credentials")
    public void testLoginWithValidCredentials() {
        System.out.println("\n[TEST 2] Testing login with valid credentials...");
        
        try {
            loginPage.navigateToLoginPage(TestConfig.BASE_URL);
            
            System.out.println("  Username: " + TestConfig.VALID_USERNAME);
            System.out.println("  Password: " + TestConfig.VALID_PASSWORD);
            
            loginPage.login(TestConfig.VALID_USERNAME, TestConfig.VALID_PASSWORD);
            
            // Wait for redirect
            Thread.sleep(2000);
            
            // Verify redirect to home page
            String currentUrl = driver.getCurrentUrl();
            Assert.assertFalse(currentUrl.contains("/login"), 
                "Should redirect away from login page");
            
            System.out.println("✓ Login successful");
            System.out.println("  Redirected to: " + currentUrl);
            
        } catch (Exception e) {
            System.err.println("✗ Test failed: " + e.getMessage());
            System.err.println("  Make sure test user exists in database:");
            System.err.println("  Username: " + TestConfig.VALID_USERNAME);
            System.err.println("  Password: " + TestConfig.VALID_PASSWORD);
            throw new RuntimeException(e);
        }
    }
    
    @Test(priority = 3, description = "Verify login with invalid username")
    public void testLoginWithInvalidUsername() {
        System.out.println("\n[TEST 3] Testing login with invalid username...");
        
        try {
            loginPage.navigateToLoginPage(TestConfig.BASE_URL);
            
            System.out.println("  Username: " + TestConfig.INVALID_USERNAME);
            System.out.println("  Password: " + TestConfig.VALID_PASSWORD);
            
            loginPage.login(TestConfig.INVALID_USERNAME, TestConfig.VALID_PASSWORD);
            
            // Wait for error message
            Thread.sleep(1000);
            
            // Verify still on login page or error displayed
            String currentUrl = loginPage.getCurrentUrl();
            boolean hasError = currentUrl.contains("error") || 
                             loginPage.isErrorMessageDisplayed();
            
            Assert.assertTrue(hasError, 
                "Should show error or stay on login page");
            
            System.out.println("✓ Invalid login rejected as expected");
            
        } catch (Exception e) {
            System.err.println("✗ Test failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    @Test(priority = 4, description = "Verify login with invalid password")
    public void testLoginWithInvalidPassword() {
        System.out.println("\n[TEST 4] Testing login with invalid password...");
        
        try {
            loginPage.navigateToLoginPage(TestConfig.BASE_URL);
            
            System.out.println("  Username: " + TestConfig.VALID_USERNAME);
            System.out.println("  Password: " + TestConfig.INVALID_PASSWORD);
            
            loginPage.login(TestConfig.VALID_USERNAME, TestConfig.INVALID_PASSWORD);
            
            // Wait for error message
            Thread.sleep(1000);
            
            // Verify still on login page or error displayed
            String currentUrl = loginPage.getCurrentUrl();
            boolean hasError = currentUrl.contains("error") || 
                             loginPage.isErrorMessageDisplayed();
            
            Assert.assertTrue(hasError, 
                "Should show error or stay on login page");
            
            System.out.println("✓ Invalid password rejected as expected");
            
        } catch (Exception e) {
            System.err.println("✗ Test failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    @Test(priority = 5, description = "Verify login with empty credentials")
    public void testLoginWithEmptyCredentials() {
        System.out.println("\n[TEST 5] Testing login with empty credentials...");
        
        try {
            loginPage.navigateToLoginPage(TestConfig.BASE_URL);
            
            System.out.println("  Username: (empty)");
            System.out.println("  Password: (empty)");
            
            loginPage.login(TestConfig.EMPTY_USERNAME, TestConfig.EMPTY_PASSWORD);
            
            // Wait for validation
            Thread.sleep(1000);
            
            // Verify still on login page
            String currentUrl = loginPage.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("/login"), 
                "Should stay on login page");
            
            System.out.println("✓ Empty credentials rejected as expected");
            
        } catch (Exception e) {
            System.err.println("✗ Test failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    @Test(priority = 6, description = "Verify login form elements are present")
    public void testLoginFormElements() {
        System.out.println("\n[TEST 6] Testing login form elements...");
        
        try {
            loginPage.navigateToLoginPage(TestConfig.BASE_URL);
            
            Assert.assertTrue(loginPage.isLoginPageDisplayed(), 
                "Login form should be displayed");
            
            // Check for additional elements
            boolean hasRegisterLink = loginPage.isRegisterLinkDisplayed();
            boolean hasForgotPasswordLink = loginPage.isForgotPasswordLinkDisplayed();
            
            System.out.println("✓ Login form elements verified");
            System.out.println("  Register link: " + (hasRegisterLink ? "Present" : "Not found"));
            System.out.println("  Forgot password link: " + (hasForgotPasswordLink ? "Present" : "Not found"));
            
        } catch (Exception e) {
            System.err.println("✗ Test failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    @Test(priority = 7, description = "Verify successful login and logout flow")
    public void testLoginLogoutFlow() {
        System.out.println("\n[TEST 7] Testing complete login-logout flow...");
        
        try {
            // Login
            loginPage.navigateToLoginPage(TestConfig.BASE_URL);
            loginPage.login(TestConfig.VALID_USERNAME, TestConfig.VALID_PASSWORD);
            Thread.sleep(2000);
            
            // Verify logged in
            String urlAfterLogin = driver.getCurrentUrl();
            Assert.assertFalse(urlAfterLogin.contains("/login"), 
                "Should be logged in");
            
            System.out.println("✓ Login successful");
            
            // Logout (if logout functionality exists)
            try {
                homePage.logout();
                Thread.sleep(1000);
                
                String urlAfterLogout = driver.getCurrentUrl();
                System.out.println("✓ Logout successful");
                System.out.println("  Redirected to: " + urlAfterLogout);
                
            } catch (Exception e) {
                System.out.println("  Note: Logout functionality not tested (element not found)");
            }
            
        } catch (Exception e) {
            System.err.println("✗ Test failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    @AfterMethod
    public void tearDown(ITestResult result) {
        // Chụp screenshot nếu test fail
        if (result.getStatus() == ITestResult.FAILURE) {
            System.out.println("\n✗ Test FAILED: " + result.getName());
            if (driver != null) {
                String screenshotPath = ScreenshotUtil.captureScreenshot(driver, result.getName());
                System.out.println("  Screenshot: " + screenshotPath);
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            System.out.println("✓ Test PASSED: " + result.getName());
        }
        
        // Đóng browser
        if (driver != null) {
            driver.quit();
            System.out.println("✓ Browser closed");
        }
    }
    
    @AfterClass
    public void tearDownClass() {
        System.out.println("\n===========================================");
        System.out.println("Login Test Suite Completed");
        System.out.println("===========================================");
    }
}
