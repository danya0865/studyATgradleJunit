
package selenide;

import config.TestConfig;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GoodsSelenideTest extends BaseSelenideTest {

    @Step("Открыть магазин")
    private void openStore() {
        open("/");
    }

    @Step("Открыть админку")
    private void openAdmin() {
        open("/admin");
    }

    @Step("Авторизоваться администратором")
    private void loginAsAdmin() {
        $("[name='username']").setValue(TestConfig.getAdminUsername());
        $("[name='password']").setValue(TestConfig.getAdminPassword());
        $("button[type='submit']").click();
    }

    @Step("Добавить товар {productName} с ценой {productPrice}")
    private void addProduct(String productName, String productPrice) {
        $("#n-name").setValue(productName);
        $("#n-price").setValue(productPrice);
        $("#add-btn").click();
    }

    @Step("Проверить отображение товара {productName}")
    private void assertProductVisible(String productName) {
        $$(".product-card h4")
                .findBy(exactText(productName))
                .shouldBe(visible);
    }

    @Step("Добавить стартовый товар в корзину")
    private void addDefaultProductToCart() {
        $("button[data-action='add-to-cart'][data-name='"
                + TestConfig.getProductName() + "']").click();
    }

    @Step("Открыть корзину")
    private void openCart() {
        $("#open-cart-btn").click();
    }

    @Step("Проверить наличие товара в корзине")
    private void assertCartContainsProduct() {
        $("#cart-items .cart-item").shouldBe(visible);
    }

    @Step("Ввести неверные учётные данные")
    private void enterInvalidCredentials() {
        $("[name='username']").setValue("wrong_user");
        $("[name='password']").setValue("wrong_password");
        $("button[type='submit']").click();
    }

    @Step("Проверить, что форма авторизации осталась открытой")
    private void assertLoginFormVisible() {
        $("[name='username']").shouldBe(visible);
    }

    @Step("Обновить страницу")
    private void refreshPage() {
        refresh();
    }

    @Step("Проверить итоговую стоимость {expectedTotal}")
    private void assertTotalPrice(String expectedTotal) {
        $("#total-price").shouldHave(exactText(expectedTotal));
    }

    @Step("Оформить заказ")
    private void makeOrder() {
        $("#makeOrder").click();
    }

    @Step("Проверить предупреждение о превышении суммы заказа")
    private void assertOrderAlertNotBlank() {
        String alertText = switchTo().alert().getText();

        assertThat(alertText).isNotBlank();

        switchTo().alert().accept();
    }

    @Test
    public void shouldAddProductThroughAdmin() {
        String productName = TestConfig.getProductName()
                + "_" + UUID.randomUUID();
        String productPrice = String.valueOf(TestConfig.getProductPrice());

        openAdmin();
        loginAsAdmin();
        addProduct(productName, productPrice);

        openStore();
        assertProductVisible(productName);
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
        enterInvalidCredentials();
        assertLoginFormVisible();
    }

    @Test
    public void shouldKeepCartAfterPageRefresh() {
        openStore();
        addDefaultProductToCart();
        openCart();
        assertCartContainsProduct();

        refreshPage();

        openCart();
        assertCartContainsProduct();
    }

    @Test
    public void shouldShowAlertWhenOrderTotalExceeds300() {
        openStore();

        addDefaultProductToCart();
        addDefaultProductToCart();

        openCart();

        String expectedTotal = String.valueOf(
                (int) (TestConfig.getProductPrice() * 2)
        );

        assertTotalPrice(expectedTotal);

        makeOrder();
        assertOrderAlertNotBlank();
    }
}
