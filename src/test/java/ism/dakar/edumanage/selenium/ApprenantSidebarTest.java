package ism.dakar.edumanage.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("selenium")
class ApprenantSidebarTest extends SeleniumBaseTest {

    private static final String EMAIL = "test.apprenant@edumanage.local";
    private static final String PASSWORD = "Test1234!";

    @Test
    @DisplayName("Un apprenant ne voit que Dashboard et Formations dans la sidebar")
    void apprenantVoitUniquementSesEntreesDeMenu() {
        driver.get(BASE_URL + "/login");

        WebElement emailInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='email']")));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[name='password']"));
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));

        emailInput.sendKeys(EMAIL);
        passwordInput.sendKeys(PASSWORD);
        submitButton.click();

        wait.until(ExpectedConditions.urlContains("/dashboard"));

        WebElement dashboardLink = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("a.sidebar-link[href='/dashboard']")));
        assertNotNull(dashboardLink, "Le lien Dashboard doit être visible pour un apprenant");

        WebElement formationsLink = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("a.sidebar-link[href='/formations']")));
        assertNotNull(formationsLink, "Le lien Formations doit être visible pour un apprenant");

        List<WebElement> paiementsLinks = driver.findElements(
                By.cssSelector("a.sidebar-link[href='/paiements']"));
        assertTrue(paiementsLinks.isEmpty(), "Le lien Paiements ne doit pas être visible pour un apprenant");

        List<WebElement> usersLinks = driver.findElements(
                By.cssSelector("a.sidebar-link[href='/users']"));
        assertTrue(usersLinks.isEmpty(), "Le lien Utilisateurs ne doit pas être visible pour un apprenant");
    }
}
