package poly.edu.java5_asm.selenium.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * WebDriver Manager để quản lý các browser drivers
 */
public class WebDriverManager {
    
    private static final String BROWSER = System.getProperty("browser", "chrome");
    private static final boolean HEADLESS = Boolean.parseBoolean(System.getProperty("headless", "true"));
    
    public static WebDriver createDriver() {
        WebDriver driver;
        
        switch (BROWSER.toLowerCase()) {
            case "firefox":
                driver = createFirefoxDriver();
                break;
            case "edge":
                driver = createEdgeDriver();
                break;
            case "chrome":
            default:
                driver = createChromeDriver();
                break;
        }
        
        driver.manage().window().maximize();
        return driver;
    }
    
    private static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        
        if (HEADLESS) {
            options.addArguments("--headless=new");
        }
        
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-notifications");
        options.addArguments("--ignore-certificate-errors");
        
        // Tắt logging
        options.addArguments("--log-level=3");
        options.addArguments("--silent");
        
        return new ChromeDriver(options);
    }
    
    private static WebDriver createFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        
        if (HEADLESS) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        
        return new FirefoxDriver(options);
    }
    
    private static WebDriver createEdgeDriver() {
        EdgeOptions options = new EdgeOptions();
        
        if (HEADLESS) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        
        return new EdgeDriver(options);
    }
}
