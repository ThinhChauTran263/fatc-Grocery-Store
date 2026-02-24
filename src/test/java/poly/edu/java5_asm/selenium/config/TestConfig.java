package poly.edu.java5_asm.selenium.config;

/**
 * Test Configuration
 */
public class TestConfig {
    
    // Base URL
    public static final String BASE_URL = System.getProperty("base.url", "http://localhost:8080");
    
    // Timeouts
    public static final int IMPLICIT_WAIT = 10;
    public static final int EXPLICIT_WAIT = 10;
    public static final int PAGE_LOAD_TIMEOUT = 30;
    
    // Test Data
    public static final String VALID_USERNAME = "admin";
    public static final String VALID_PASSWORD = "123";
    
    public static final String INVALID_USERNAME = "invaliduser";
    public static final String INVALID_PASSWORD = "wrongpassword";
    
    public static final String EMPTY_USERNAME = "";
    public static final String EMPTY_PASSWORD = "";
    
    // Screenshots
    public static final String SCREENSHOT_PATH = "target/screenshots/";
    
    // Reports
    public static final String REPORT_PATH = "target/test-reports/";
}
