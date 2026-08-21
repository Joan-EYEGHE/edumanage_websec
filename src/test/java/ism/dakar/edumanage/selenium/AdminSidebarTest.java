package ism.dakar.edumanage.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("selenium")
class AdminSidebarTest extends SeleniumBaseTest {

    private static final String EMAIL = "joanarcher26@gmail.com";
    private static final String PASSWORD = "joan@admin2";

    @Test
    @DisplayName("Un administrateur voit les 6 entrées de menu dans la sidebar")
    void adminVoitLesSixEntreesDeMenu() {
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
        assertNotNull(dashboardLink, "Dashboard doit être visible pour un admin");

        WebElement usersLink = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("a.sidebar-link[href='/users']")));
        assertNotNull(usersLink, "Utilisateurs doit être visible pour un admin");

        WebElement formationsLink = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("a.sidebar-link[href='/formations']")));
        assertNotNull(formationsLink, "Formations doit être visible pour un admin");

        WebElement inscriptionsLink = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("a.sidebar-link[href='/inscriptions']")));
        assertNotNull(inscriptionsLink, "Inscriptions doit être visible pour un admin");

        WebElement paiementsLink = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("a.sidebar-link[href='/paiements']")));
        assertNotNull(paiementsLink, "Paiements doit être visible pour un admin");

        WebElement auditLogsLink = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("a.sidebar-link[href='/audit-logs']")));
        assertNotNull(auditLogsLink, "Audit Logs doit être visible pour un admin");
    }
}