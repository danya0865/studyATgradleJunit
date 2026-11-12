
package selenide;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GoodsSelenideTest extends BaseSelenideTest {

    @Test
    public void shouldAddProductThroughAdmin() {
        String productName = "Selenide_" + UUID.randomUUID();
        String productPrice = "250";

        // Открываем админку
        open("/admin");

        // Авторизуемся
        $("[name='username']").setValue("admin");
        $("[name='password']").setValue("secret123");
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

        $("button[data-action='add-to-cart'][data-name='Стакан']").click();

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

        $("button[data-action='add-to-cart'][data-name='Стакан']").click();

        $("#open-cart-btn").click();
        $("#cart-items .cart-item").shouldBe(visible);

        refresh();

        $("#open-cart-btn").click();
        $("#cart-items .cart-item").shouldBe(visible);
    }

    @Test
    public void shouldShowAlertWhenOrderTotalExceeds300() {
        open("/");

        $("button[data-action='add-to-cart'][data-name='Стакан']").click();
        $("button[data-action='add-to-cart'][data-name='Стакан']").click();

        $("#open-cart-btn").click();

        $("#total-price").shouldHave(exactText("340"));

        $("#makeOrder").click();

        String alertText = switchTo().alert().getText();

        assertThat(alertText).isNotBlank();

        switchTo().alert().accept();
    }
}
