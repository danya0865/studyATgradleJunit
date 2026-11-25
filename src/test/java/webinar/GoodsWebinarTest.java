
package webinar;

import config.TestConfig;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class GoodsWebinarTest extends BaseWebinarTest {

    @Test
    public void shouldCreateOrderWithThreeItems() {
        // Создаём тестовый товар через API
        String productName = TestConfig.getProductName()
                + "_" + java.util.UUID.randomUUID();

        double productPrice = TestConfig.getProductPrice();

        long productId = createGood(productName, productPrice);

        // Открываем главную страницу
        open("/");

        // Добавляем товар в корзину три раза
        String addButton = "button[data-action='add-to-cart'][data-id='" + productId + "']";

        $(addButton).shouldBe(visible).click();
        $(addButton).click();
        $(addButton).click();

        // Открываем корзину
        $("#open-cart-btn").click();

        // Проверяем общую сумму
        $("#total-price").shouldHave(
                exactText(String.valueOf((int) (productPrice * 3)))
        );

        // Оформляем заказ
        $("#makeOrder").click();

        // Проверяем уведомление об успешном оформлении
        $$("#toast-container .toast")
                .findBy(exactText("Заказ принят в обработку!"))
                .shouldBe(visible);
    }

    @Test
    public void shouldCalculateTotalPriceForDifferentProducts() {
        // Создаём два разных товара через API
        String firstName = "Webinar_First_" + java.util.UUID.randomUUID();
        String secondName = "Webinar_Second_" + java.util.UUID.randomUUID();

        long firstId = createGood(firstName, 100);
        long secondId = createGood(secondName, 150);

        // Открываем витрину
        open("/");

        // Добавляем оба товара в корзину
        String firstButton = "button[data-action='add-to-cart'][data-id='" + firstId + "']";
        String secondButton = "button[data-action='add-to-cart'][data-id='" + secondId + "']";

        $(firstButton).shouldBe(visible).click();
        $(secondButton).shouldBe(visible).click();

        // Открываем корзину
        $("#open-cart-btn").click();

        // Проверяем общую стоимость
        int expectedTotal = (int) (
                TestConfig.getProductPrice() +
                        TestConfig.getProductPrice()
        );

        $("#total-price").shouldHave(exactText(String.valueOf(expectedTotal)));

        $("#total-price").shouldHave(exactText(String.valueOf(expectedTotal)));
    }

    @Test
    public void shouldAddProductThroughAdminAndShowNotification() {
        String productName = TestConfig.getProductName()
                + "_" + java.util.UUID.randomUUID();

        double productPrice = TestConfig.getProductPrice();

        // Открываем админку
        open("/admin");

        // Авторизуемся
        $("[name='username']").setValue(TestConfig.getAdminUsername());
        $("[name='password']").setValue(TestConfig.getAdminPassword());
        $("button[type='submit']").click();

        // Заполняем форму добавления товара
        $("#n-name").setValue(productName);
        $("#n-price").setValue(String.valueOf((int) productPrice));

        // Добавляем товар
        $("#add-btn").click();

        // Проверяем уведомление
        $$("#toast-container .toast")
                .findBy(exactText("Товар успешно добавлен!"))
                .shouldBe(visible);
    }

    @Test
    public void shouldEditProductThroughAdmin() {
        // Создаём товар через API
        String productName = "Webinar_Edit_" + java.util.UUID.randomUUID();
        long productId = createGood(productName, 100);

        String newName = productName + "_Updated";
        String newPrice = "250";

        // Открываем админку
        open("/admin");

        // Если авторизация уже активна, форма входа не понадобится
        if ($("[name='username']").exists()) {
            $("[name='username']").setValue(TestConfig.getAdminUsername());
            $("[name='password']").setValue(TestConfig.getAdminPassword());
            $("button[type='submit']").click();
        }

        // Изменяем название и цену
        $("#nm-" + productId).setValue(newName);
        $("#pr-" + productId).setValue(newPrice);

        // Сохраняем изменения
        $("button[data-action='update'][data-id='" + productId + "']").click();

        // Проверяем изменения в таблице
        $("#nm-" + productId).shouldHave(
                com.codeborne.selenide.Condition.value(newName)
        );
        $("#pr-" + productId).shouldHave(
                com.codeborne.selenide.Condition.value(newPrice)
        );
    }
}
