
package webinar;

import config.TestConfig;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class GoodsWebinarTest extends BaseWebinarTest {

    @Step("Открыть главную страницу магазина")
    private void openStore() {
        open("/");
    }

    @Step("Открыть админку")
    private void openAdmin() {
        open("/admin");
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

    @Step("Авторизоваться администратором")
    private void loginAsAdmin() {
        $("[name='username']").setValue(TestConfig.getAdminUsername());
        $("[name='password']").setValue(TestConfig.getAdminPassword());
        $("button[type='submit']").click();
    }

    @Step("Добавить товар через админку")
    private void addProductThroughAdmin(String productName, double productPrice) {
        $("#n-name").setValue(productName);
        $("#n-price").setValue(String.valueOf((int) productPrice));
        $("#add-btn").click();
    }

    @Step("Проверить уведомление об успешном добавлении товара")
    private void assertProductAdded() {
        $$("#toast-container .toast")
                .findBy(exactText("Товар успешно добавлен!"))
                .shouldBe(visible);
    }

    @Step("Авторизоваться, если форма входа отображается")
    private void loginIfRequired() {
        if ($("[name='username']").exists()) {
            loginAsAdmin();
        }
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
        // Создаём тестовый товар через API
        String productName = TestConfig.getProductName()
                + "_" + java.util.UUID.randomUUID();

        double productPrice = 100;

        long productId = createGood(productName, productPrice);

        // Добавляем товар в корзину три раза
        openStore();
        addProductToCartThreeTimes(productId);

        // Проверяем сумму и оформляем заказ
        openCart();

        int expectedTotal = (int) (productPrice * 3);
        assertTotalPrice(expectedTotal);

        makeOrder();
        assertOrderAccepted();
    }

    @Test
    public void shouldCalculateTotalPriceForDifferentProducts() {
        // Создаём два разных товара через API
        String firstName = "Webinar_First_" + java.util.UUID.randomUUID();
        String secondName = "Webinar_Second_" + java.util.UUID.randomUUID();

        long firstId = createGood(firstName, 100);
        long secondId = createGood(secondName, 150);

        // Добавляем оба товара в корзину
        openStore();
        addProductToCart(firstId);
        addProductToCart(secondId);

        // Проверяем общую стоимость
        openCart();

        int expectedTotal = 100 + 150;
        assertTotalPrice(expectedTotal);
    }

    @Test
    public void shouldAddProductThroughAdminAndShowNotification() {
        String productName = TestConfig.getProductName()
                + "_" + java.util.UUID.randomUUID();

        double productPrice = TestConfig.getProductPrice();

        // Добавляем товар через админку
        openAdmin();
        loginAsAdmin();
        addProductThroughAdmin(productName, productPrice);

        // Проверяем уведомление
        assertProductAdded();
    }

    @Test
    public void shouldEditProductThroughAdmin() {
        // Создаём товар через API
        String productName = "Webinar_Edit_" + java.util.UUID.randomUUID();
        long productId = createGood(productName, 100);

        String newName = productName + "_Updated";
        String newPrice = "250";

        // Открываем админку и авторизуемся при необходимости
        openAdmin();
        loginIfRequired();

        // Изменяем товар и проверяем результат
        updateProduct(productId, newName, newPrice);
        assertProductUpdated(productId, newName, newPrice);
    }
}
