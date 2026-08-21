package ism.dakar.edumanage.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Tag("selenium")
public abstract class SeleniumBaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    static final String BASE_URL = "http://localhost:5173";

    @BeforeAll
    static void setupDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupBrowser() {
        ChromeOptions options = new ChromeOptions();
        // Retire cette ligne si tu veux voir le navigateur s'ouvrir (recommandé pour les captures)
        // options.addArguments("--headless");
        options.setExperimentalOption("detach", true);
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}