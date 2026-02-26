package com.oceanviewsystem.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SeleniumUITest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
    }

    @Test
    public void testUserLoginUI() throws InterruptedException {
        driver.get("http://localhost:8080/OceanViewSystem_war_exploded/login.jsp");
        Thread.sleep(2000);
        WebElement usernameField = driver.findElement(By.name("username"));
        WebElement passwordField = driver.findElement(By.name("password"));

        usernameField.sendKeys("admin");
        passwordField.sendKeys("admin123");

        WebElement loginButton = driver.findElement(By.tagName("button")); // බොත්තමේ tag එක හෝ ID එක දෙන්න
        loginButton.click();

        Thread.sleep(2000);
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("dashboard.jsp") || currentUrl.contains("index.jsp"),
                "Login UI Test Failed: Not redirected to dashboard");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}