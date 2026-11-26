
package selenium;

import config.TestConfig;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GoodsSeleniumTest extends BaseSeleniumTest {

    private WebDriverWait getWait() {
        return new WebDriverWait(
                driver, Duration.ofSeconds(TestConfig.getTimeoutSeconds())
        );
    }

    @Step("Открыть страницу магазина")
    private void openStore() {
        driver.get(TestConfig.getAppUrl());
    }

    @Step("Открыть страницу администратора")
    private void openAdmin() {
        driver.get(TestConfig.getAppUrl() + "/admin");
    }

    @Step("Авторизоваться администратором")
    private void loginAsAdmin() {
        getWait().until(ExpectedConditions.visibilityOfElementLocated(
                By.name("username")
        )).sendKeys(TestConfig.getAdminUsername());

        driver.findElement(By.name("password"))
                .sendKeys(TestConfig.getAdminPassword());

        driver.findElement(By.cssSelector("button[type='submit']"))
                .click();
    }

    @Step("Заполнить форму товара: {productName}, цена {productPrice}")
    private void fillProductForm(String productName, String productPrice) {
        getWait().until(ExpectedConditions.visibilityOfElementLocated(
                By.id("n-name")
        )).sendKeys(productName);

        driver.findElement(By.id("n-price"))
                .sendKeys(productPrice);
    }

    @Step("Добавить товар")
    private void submitProduct() {
        driver.findElement(By.id("add-btn")).click();
    }

    @Step("Проверить отображение товара {productName} в магазине")
    private void assertProductDisplayed(String productName) {
        boolean productIsDisplayed = getWait().until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath(
                                "//div[contains(@class,'product-card')]" +
                                        "[.//h4[normalize-space()='" +
                                        productName + "']]"
                        )
                )
        ).isDisplayed();

        assertTrue(
                productIsDisplayed,
                "Добавленный товар должен отображаться в магазине"
        );
    }

    @Step("Добавить стартовый товар в корзину")
    private void addDefaultProductToCart() {
        String productSelector =
                "button[data-action='add-to-cart'][data-name='"
                        + TestConfig.getProductName() + "']";

        getWait().until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(productSelector)
        )).click();
    }

    @Step("Открыть корзину")
    private void openCart() {
        driver.findElement(By.id("open-cart-btn")).click();
    }

    @Step("Проверить наличие товара в корзине")
    private void assertCartContainsProduct() {
        boolean productIsDisplayed = getWait().until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("#cart-items .cart-item")
                )
        ).isDisplayed();

        assertTrue(
                productIsDisplayed,
                "Товар должен отображаться в корзине"
        );
    }

    @Step("Ввести неверные учётные данные администратора")
    private void loginWithInvalidCredentials() {
        getWait().until(ExpectedConditions.visibilityOfElementLocated(
                By.name("username")
        )).sendKeys("wrong_user");

        getWait().until(ExpectedConditions.visibilityOfElementLocated(
                By.name("password")
        )).sendKeys("wrong_password");

        getWait().until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button[type='submit']")
        )).click();
    }

    @Step("Проверить, что авторизация не выполнена")
    private void assertLoginFormDisplayed() {
        // Spring Security после неудачного входа перенаправляет на /login?error
        boolean loginFailed = getWait().until(
                ExpectedConditions.urlContains("error")
        );

        assertTrue(
                loginFailed,
                "При неверных данных пользователь не должен войти в админку"
        );
    }

    @Step("Обновить страницу")
    private void refreshPage() {
        driver.navigate().refresh();
    }

    @Step("Проверить, что в корзине ровно один товар")
    private void assertOneProductInCart() {
        getWait().until(ExpectedConditions.numberOfElementsToBe(
                By.cssSelector("#cart-items .cart-item"), 1
        ));

        assertTrue(
                driver.findElement(
                        By.cssSelector("#cart-items .cart-item")
                ).isDisplayed(),
                "Корзина должна сохранять товар после обновления страницы"
        );
    }

    @Test
    public void shouldAddProductThroughAdmin() {
        String productName = TestConfig.getProductName()
                + "_" + UUID.randomUUID();
        String productPrice = String.valueOf(TestConfig.getProductPrice());

        openAdmin();
        loginAsAdmin();
        fillProductForm(productName, productPrice);
        submitProduct();

        openStore();
        assertProductDisplayed(productName);
    }

    @Test
    public void shouldAddProductToCart() {
        openStore();
        addDefaultProductToCart();
        openCart();
        assertCartContainsProduct();
    }

    @Test
    public void shouldRejectInvalidAdminCredentials() {
        openAdmin();
        loginWithInvalidCredentials();
        assertLoginFormDisplayed();
    }

    @Test
    public void shouldKeepCartAfterPageRefresh() {
        openStore();
        addDefaultProductToCart();
        openCart();
        assertCartContainsProduct();

        refreshPage();
        openCart();
        assertOneProductInCart();
    }
}
