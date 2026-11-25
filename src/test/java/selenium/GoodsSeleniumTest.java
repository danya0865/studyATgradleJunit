
package selenium;

import config.TestConfig;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GoodsSeleniumTest extends BaseSeleniumTest {

    @Test
    public void shouldAddProductThroughAdmin() {
        String productName = TestConfig.getProductName() + "_" + UUID.randomUUID();
        String productPrice = String.valueOf(TestConfig.getProductPrice());

        WebDriverWait wait = new WebDriverWait(
                driver, Duration.ofSeconds(TestConfig.getTimeoutSeconds())
        );

        // Открываем страницу администратора
        driver.get(TestConfig.getAppUrl() + "/admin");

        // Авторизуемся
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("username")
        )).sendKeys(TestConfig.getAdminUsername());

        driver.findElement(By.name("password"))
                .sendKeys(TestConfig.getAdminPassword());

        driver.findElement(By.cssSelector("button[type='submit']"))
                .click();

        // Заполняем форму добавления товара
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("n-name")
        )).sendKeys(productName);

        driver.findElement(By.id("n-price"))
                .sendKeys(productPrice);

        driver.findElement(By.id("add-btn"))
                .click();

        // Возвращаемся в магазин
        driver.get(TestConfig.getAppUrl());

        // Проверяем, что товар появился
        boolean productIsDisplayed = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath(
                                "//div[contains(@class,'product-card')]" +
                                        "[.//h4[normalize-space()='" +
                                        productName + "']]"
                        )
                )
        ).isDisplayed();

        assertTrue(productIsDisplayed,
                "Добавленный товар должен отображаться в магазине");
    }

    @Test
    public void shouldAddProductToCart() {
        WebDriverWait wait = new WebDriverWait(
                driver, Duration.ofSeconds(TestConfig.getTimeoutSeconds())
        );

        driver.get(TestConfig.getAppUrl());

        // Добавляем стартовый товар
        String productSelector =
                "button[data-action='add-to-cart'][data-name='"
                        + TestConfig.getProductName() + "']";

        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(productSelector)
        )).click();

        // Открываем корзину
        driver.findElement(By.id("open-cart-btn")).click();

        // Проверяем наличие товара в корзине
        boolean productIsDisplayed = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("#cart-items .cart-item")
                )
        ).isDisplayed();

        assertTrue(productIsDisplayed,
                "Товар должен отображаться в корзине");
    }

    @Test
    public void shouldRejectInvalidAdminCredentials() {
        WebDriverWait wait = new WebDriverWait(
                driver, Duration.ofSeconds(TestConfig.getTimeoutSeconds())
        );

        driver.get(TestConfig.getAppUrl() + "/admin");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("username")
        )).sendKeys("wrong_user");

        driver.findElement(By.name("password"))
                .sendKeys("wrong_password");

        driver.findElement(By.cssSelector("button[type='submit']"))
                .click();

        // Проверяем, что вход не выполнен
        boolean loginFormIsDisplayed = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.name("username")
                )
        ).isDisplayed();

        assertTrue(loginFormIsDisplayed,
                "При неверных данных пользователь не должен войти в админку");
    }

    @Test
    public void shouldKeepCartAfterPageRefresh() {
        WebDriverWait wait = new WebDriverWait(
                driver, Duration.ofSeconds(TestConfig.getTimeoutSeconds())
        );

        driver.get(TestConfig.getAppUrl());

        // Добавляем стартовый товар
        String productSelector =
                "button[data-action='add-to-cart'][data-name='"
                        + TestConfig.getProductName() + "']";

        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(productSelector)
        )).click();

        // Открываем корзину
        driver.findElement(By.id("open-cart-btn")).click();

        // Проверяем, что товар добавлен
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#cart-items .cart-item")
        ));

        // Обновляем страницу
        driver.navigate().refresh();

        // Снова открываем корзину
        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("open-cart-btn")
        )).click();

        // Проверяем, что товар сохранился
        wait.until(ExpectedConditions.numberOfElementsToBe(
                By.cssSelector("#cart-items .cart-item"), 1
        ));

        assertTrue(
                driver.findElement(
                        By.cssSelector("#cart-items .cart-item")
                ).isDisplayed(),
                "Корзина должна сохранять товар после обновления страницы"
        );
    }
}
