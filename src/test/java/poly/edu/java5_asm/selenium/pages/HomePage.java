package poly.edu.java5_asm.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model cho Home Page
 */
public class HomePage {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    // Locators
    @FindBy(css = ".navbar-brand, .logo")
    private WebElement logo;
    
    @FindBy(css = ".user-menu, .dropdown-toggle")
    private WebElement userMenu;
    
    @FindBy(linkText = "Đăng xuất")
    private WebElement logoutLink;
    
    @FindBy(css = ".welcome-message, .user-name")
    private WebElement welcomeMessage;
    
    @FindBy(css = ".cart-icon, .shopping-cart")
    private WebElement cartIcon;
    
    @FindBy(linkText = "Sản phẩm")
    private WebElement productsLink;
    
    // Constructor
    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }
    
    // Actions
    public void navigateToHomePage(String baseUrl) {
        driver.get(baseUrl);
        waitForPageLoad();
    }
    
    public void clickUserMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(userMenu));
        userMenu.click();
    }
    
    public void logout() {
        clickUserMenu();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink));
        logoutLink.click();
    }
    
    // Verifications
    public boolean isUserLoggedIn() {
        try {
            wait.until(ExpectedConditions.visibilityOf(userMenu));
            return userMenu.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isLogoDisplayed() {
        try {
            return logo.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getWelcomeMessage() {
        try {
            wait.until(ExpectedConditions.visibilityOf(welcomeMessage));
            return welcomeMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }
    
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    private void waitForPageLoad() {
        wait.until(driver -> 
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete")
        );
    }
}
