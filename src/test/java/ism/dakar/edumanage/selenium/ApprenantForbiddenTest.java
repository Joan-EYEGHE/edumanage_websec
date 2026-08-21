package ism.dakar.edumanage.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("selenium")
class ApprenantForbiddenTest extends SeleniumBaseTest {

    private static final String EMAIL = "test.apprenant@edumanage.local";
    private static final String PASSWORD = "Test1234!";

    @Test
    @DisplayName("Un apprenant redirigé vers /forbidden en accédant à /paiements")
    void apprenantRedirigéVersForbiddenSurPaiements() {
        driver.get(BASE_URL + "/login");

        WebElement emailInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='email']")));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[name='password']"));
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));

        emailInput.sendKeys(EMAIL);
        passwordInput.sendKeys(PASSWORD);
        submitButton.click();

        wait.until(ExpectedConditions.urlContains("/dashboard"));

        driver.get(BASE_URL + "/paiements");

        wait.until(ExpectedConditions.urlContains("/forbidden"));

        assertTrue(
                driver.getCurrentUrl().contains("/forbidden"),
                "Un apprenant accédant à /paiements doit être redirigé vers /forbidden"
        );
    }
}