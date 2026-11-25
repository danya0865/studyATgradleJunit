
package selenide;

import config.TestConfig;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GoodsSelenideTest extends BaseSelenideTest {

    @Test
    public void shouldAddProductThroughAdmin() {
        String productName = TestConfig.getProductName() + "_" + UUID.randomUUID();
        String productPrice = String.valueOf(TestConfig.getProductPrice());

        // Открываем админку
        open("/admin");

        // Авторизуемся
        $("[name='username']").setValue(TestConfig.getAdminUsername());
        $("[name='password']").setValue(TestConfig.getAdminPassword());
        $("button[type='submit']").click();

        // Добавляем товар
        $("#n-name").setValue(productName);
        $("#n-price").setValue(productPrice);
        $("#add-btn").click();

        // Переходим в магазин
        open("/");

        // Проверяем, что товар появился
        $$(".product-card h4")
                .findBy(exactText(productName))
                .shouldBe(visible);
    }

    @Test
    public void shouldAddProductToCart() {
        open("/");

        // Добавляем стартовый товар
        $("button[data-action='add-to-cart'][data-name='"
                + TestConfig.getProductName() + "']").click();

        $("#open-cart-btn").click();

        $("#cart-items .cart-item").shouldBe(visible);
    }

    @Test
    public void shouldRejectInvalidAdminCredentials() {
        open("/admin");

        $("[name='username']").setValue("wrong_user");
        $("[name='password']").setValue("wrong_password");

        $("button[type='submit']").click();

        $("[name='username']").shouldBe(visible);
    }

    @Test
    public void shouldKeepCartAfterPageRefresh() {
        open("/");

        // Добавляем стартовый товар
        $("button[data-action='add-to-cart'][data-name='"
                + TestConfig.getProductName() + "']").click();

        $("#open-cart-btn").click();
        $("#cart-items .cart-item").shouldBe(visible);

        refresh();

        $("#open-cart-btn").click();
        $("#cart-items .cart-item").shouldBe(visible);
    }

    @Test
    public void shouldShowAlertWhenOrderTotalExceeds300() {
        open("/");

        String productSelector = "button[data-action='add-to-cart'][data-name='"
                + TestConfig.getProductName() + "']";

        $(productSelector).click();
        $(productSelector).click();

        $("#open-cart-btn").click();

        String expectedTotal = String.valueOf(
                (int) (TestConfig.getProductPrice() * 2)
        );

        $("#total-price").shouldHave(exactText(expectedTotal));

        $("#makeOrder").click();

        String alertText = switchTo().alert().getText();

        assertThat(alertText).isNotBlank();

        switchTo().alert().accept();
    }
}
