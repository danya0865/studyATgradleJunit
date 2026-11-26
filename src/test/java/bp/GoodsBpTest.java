
package bp;

import config.TestConfig;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class GoodsBpTest extends BaseBpTest {

    @Step("Открыть главную страницу магазина")
    private void openStore() {
        open("/");
    }

    @Step("Найти стартовый товар")
    private com.codeborne.selenide.SelenideElement findDefaultProduct() {
        return $$(".product-card")
                .findBy(attribute("data-name", TestConfig.getProductName()));
    }

    @Step("Получить ID товара")
    private String getProductId(
            com.codeborne.selenide.SelenideElement product
    ) {
        return product.getAttribute("data-id");
    }

    @Step("Перетащить товар в корзину")
    private void dragProductToCart(
            com.codeborne.selenide.SelenideElement product
    ) {
        actions()
                .clickAndHold(product)
                .moveToElement($("#open-cart-btn"))
                .release()
                .perform();
    }

    @Step("Добавить товар в корзину")
    private void addProductToCart(
            com.codeborne.selenide.SelenideElement product
    ) {
        product.$("button[data-action='add-to-cart']").click();
    }

    @Step("Открыть корзину")
    private void openCart() {
        $("#open-cart-btn").click();
    }

    @Step("Проверить, что товар отображается в корзине")
    private void assertProductInCart(String productId) {
        $("#cart-item-" + productId).shouldBe(visible);
    }

    @Step("Удалить товар из корзины")
    private void removeProductFromCart(String productId) {
        $("#cart-item-" + productId)
                .$("button[data-action='remove']")
                .click();
    }

    @Step("Проверить, что товар удалён из корзины")
    private void assertProductRemoved(String productId) {
        $("#cart-item-" + productId).shouldNotBe(visible);
    }

    @Test
    public void shouldDragProductToCart() {
        openStore();

        var product = findDefaultProduct();
        String productId = getProductId(product);

        dragProductToCart(product);
        openCart();

        assertProductInCart(productId);
    }

    @Test
    public void shouldRemoveProductFromCart() {
        openStore();

        var product = findDefaultProduct();
        String productId = getProductId(product);

        addProductToCart(product);
        openCart();

        assertProductInCart(productId);

        removeProductFromCart(productId);

        assertProductRemoved(productId);
    }
}
