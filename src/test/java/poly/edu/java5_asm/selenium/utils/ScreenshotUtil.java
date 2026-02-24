package poly.edu.java5_asm.selenium.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import poly.edu.java5_asm.selenium.config.TestConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility để chụp screenshot khi test fail
 */
public class ScreenshotUtil {
    
    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            // Tạo thư mục screenshots nếu chưa có
            Files.createDirectories(Paths.get(TestConfig.SCREENSHOT_PATH));
            
            // Tạo tên file với timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = TestConfig.SCREENSHOT_PATH + fileName;
            
            // Chụp screenshot
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            
            // Copy file
            Files.copy(srcFile.toPath(), destFile.toPath());
            
            System.out.println("Screenshot saved: " + filePath);
            return filePath;
            
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
