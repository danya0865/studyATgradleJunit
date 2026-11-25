package bp;

import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class GoodsBpTest extends BaseBpTest {

    @Test
    public void shouldDragProductToCart() {
        // Открываем главную страницу
        open("/");

        // Находим товар "Стакан"
        var product = $$(".product-card")
                .findBy(com.codeborne.selenide.Condition.attribute("data-name", "Стакан"));

        // Получаем ID товара
        String productId = product.getAttribute("data-id");

        // Перетаскиваем товар в корзину
        actions()
                .clickAndHold(product)
                .moveToElement($("#open-cart-btn"))
                .release()
                .perform();

        // Открываем корзину
        $("#open-cart-btn").click();

        // Проверяем, что товар появился в корзине
        $("#cart-item-" + productId).shouldBe(visible);
    }

    @Test
    public void shouldRemoveProductFromCart() {
        // Открываем главную страницу
        open("/");

        // Находим товар "Стакан"
        var product = $$(".product-card")
                .findBy(com.codeborne.selenide.Condition.attribute("data-name", "Стакан"));

        // Получаем ID товара
        String productId = product.getAttribute("data-id");

        // Добавляем товар в корзину
        product.$("button[data-action='add-to-cart']").click();

        // Открываем корзину
        $("#open-cart-btn").click();

        // Проверяем, что товар появился
        $("#cart-item-" + productId).shouldBe(visible);

        // Удаляем товар
        $("#cart-item-" + productId)
                .$("button[data-action='remove']")
                .click();

        // Проверяем, что товара больше нет
        $("#cart-item-" + productId).shouldNotBe(visible);
    }
}