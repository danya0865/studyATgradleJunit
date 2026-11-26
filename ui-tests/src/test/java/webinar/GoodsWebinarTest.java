
package webinar;

import base.BaseUiTest;
import config.TestConfig;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;
import pages.AdminLoginPage;
import pages.AdminPage;

import java.util.UUID;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class GoodsWebinarTest extends BaseUiTest {

    private final AdminLoginPage adminLoginPage = new AdminLoginPage();

    @Step("Открыть главную страницу магазина")
    private void openStore() {
        open("/");
    }

    @Step("Открыть админку")
    private void openAdmin() {
        open("/admin");
    }

    @Step("Авторизоваться администратором")
    private AdminPage loginAsAdmin() {
        adminLoginPage.openPage();

        return adminLoginPage
                .enterUsername(TestConfig.getAdminUsername())
                .enterPassword(TestConfig.getAdminPassword())
                .login();
    }

    @Step("Добавить товар через админку")
    private String createProduct(String productName, String productPrice) {
        AdminPage adminPage = loginAsAdmin();

        adminPage.addProduct(productName, productPrice);

        adminPage.getNotification("Товар успешно добавлен!")
                .shouldBe(visible);

        return adminPage.getProductIdByName(productName);
    }

    @Step("Добавить товар с ID {productId} в корзину")
    private void addProductToCart(long productId) {
        String addButton =
                "button[data-action='add-to-cart'][data-id='" + productId + "']";

        $(addButton).shouldBe(visible).click();
    }

    @Step("Добавить товар в корзину три раза")
    private void addProductToCartThreeTimes(long productId) {
        addProductToCart(productId);
        addProductToCart(productId);
        addProductToCart(productId);
    }

    @Step("Открыть корзину")
    private void openCart() {
        $("#open-cart-btn").click();
    }

    @Step("Проверить общую сумму заказа: {expectedTotal}")
    private void assertTotalPrice(int expectedTotal) {
        $("#total-price").shouldHave(exactText(String.valueOf(expectedTotal)));
    }

    @Step("Оформить заказ")
    private void makeOrder() {
        $("#makeOrder").click();
    }

    @Step("Проверить уведомление об успешном оформлении заказа")
    private void assertOrderAccepted() {
        $$("#toast-container .toast")
                .findBy(exactText("Заказ принят в обработку!"))
                .shouldBe(visible);
    }

    @Step("Изменить название и цену товара")
    private void updateProduct(
            long productId,
            String newName,
            String newPrice
    ) {
        $("#nm-" + productId).setValue(newName);
        $("#pr-" + productId).setValue(newPrice);

        $("button[data-action='update'][data-id='" + productId + "']")
                .click();
    }

    @Step("Проверить обновлённые данные товара")
    private void assertProductUpdated(
            long productId,
            String newName,
            String newPrice
    ) {
        $("#nm-" + productId).shouldHave(value(newName));
        $("#pr-" + productId).shouldHave(value(newPrice));
    }

    @Test
    public void shouldCreateOrderWithThreeItems() {
        String productName = TestConfig.getProductName()
                + "_" + UUID.randomUUID();

        String productId = createProduct(productName, "100");

        openStore();
        addProductToCartThreeTimes(Long.parseLong(productId));

        openCart();

        assertTotalPrice(300);

        makeOrder();
        assertOrderAccepted();
    }

    @Test
    public void shouldCalculateTotalPriceForDifferentProducts() {
        String firstName = "Webinar_First_" + UUID.randomUUID();
        String secondName = "Webinar_Second_" + UUID.randomUUID();

        String firstId = createProduct(firstName, "100");
        String secondId = createProduct(secondName, "150");

        openStore();

        addProductToCart(Long.parseLong(firstId));
        addProductToCart(Long.parseLong(secondId));

        openCart();

        assertTotalPrice(250);
    }

    @Test
    public void shouldAddProductThroughAdminAndShowNotification() {
        String productName = TestConfig.getProductName()
                + "_" + UUID.randomUUID();

        String productPrice = String.valueOf(TestConfig.getProductPrice());

        createProduct(productName, productPrice);
    }

    @Test
    public void shouldEditProductThroughAdmin() {
        String productName = "Webinar_Edit_" + UUID.randomUUID();

        String productId = createProduct(productName, "100");

        String newName = productName + "_Updated";
        String newPrice = "250";

        openAdmin();

        updateProduct(
                Long.parseLong(productId),
                newName,
                newPrice
        );

        assertProductUpdated(
                Long.parseLong(productId),
                newName,
                newPrice
        );
    }
}
