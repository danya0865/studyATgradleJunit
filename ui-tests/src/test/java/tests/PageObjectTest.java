
package tests;
import org.junit.jupiter.api.Tag;
import assertions.PageAssert;
import base.BaseUiTest;
import config.TestConfig;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;
import pages.AdminLoginPage;
import pages.AdminPage;
import pages.MainPage;

import java.util.UUID;

@Tag("smoke")
public class PageObjectTest extends BaseUiTest {

    private final MainPage mainPage = new MainPage();
    private final AdminLoginPage adminLoginPage = new AdminLoginPage();

    @Step("Авторизоваться под администратором")
    private AdminPage loginAsAdmin() {
        adminLoginPage.openPage();

        PageAssert.assertAdminLoginFormLoaded(adminLoginPage);

        AdminPage adminPage = adminLoginPage
                .enterUsername(TestConfig.getAdminUsername())
                .enterPassword(TestConfig.getAdminPassword())
                .login();

        PageAssert.assertAdminPageLoaded(adminPage);

        return adminPage;
    }

    @Test
    public void shouldCreateOrderWithThreeItems() {
        String productName = "PageObject_" + UUID.randomUUID();
        String productPrice = "90";

        AdminPage adminPage = loginAsAdmin();
        adminPage.addProduct(productName, productPrice);

        PageAssert.assertNotificationVisible(
                adminPage.getNotification("Товар успешно добавлен!")
        );

        adminPage.returnToStore();

        PageAssert.assertMainPageLoaded(mainPage);

        mainPage.addProductToCartByName(productName);
        mainPage.addProductToCartByName(productName);
        mainPage.addProductToCartByName(productName);

        mainPage.openCart();

        PageAssert.assertCartOpened(mainPage);
        PageAssert.assertTotalPrice(mainPage, "270");

        mainPage.makeOrder();

        PageAssert.assertNotificationVisible(
                mainPage.getNotification("Заказ принят в обработку!")
        );
    }

    @Test
    public void shouldCalculateTotalPriceForDifferentProducts() {
        String firstProduct = "First_" + UUID.randomUUID();
        String secondProduct = "Second_" + UUID.randomUUID();

        AdminPage adminPage = loginAsAdmin();

        adminPage.addProduct(firstProduct, "100");

        PageAssert.assertNotificationVisible(
                adminPage.getNotification("Товар успешно добавлен!")
        );

        adminPage.addProduct(secondProduct, "150");

        PageAssert.assertNotificationVisible(
                adminPage.getNotification("Товар успешно добавлен!")
        );

        adminPage.returnToStore();

        PageAssert.assertMainPageLoaded(mainPage);

        mainPage.addProductToCartByName(firstProduct);
        mainPage.addProductToCartByName(secondProduct);

        mainPage.openCart();

        PageAssert.assertCartOpened(mainPage);
        PageAssert.assertTotalPrice(mainPage, "250");
    }

    @Test
    public void shouldAddProductThroughAdmin() {
        String productName = "New_" + UUID.randomUUID();
        String productPrice = String.valueOf(TestConfig.getProductPrice());

        AdminPage adminPage = loginAsAdmin();

        adminPage.addProduct(productName, productPrice);

        PageAssert.assertNotificationVisible(
                adminPage.getNotification("Товар успешно добавлен!")
        );
    }

    @Test
    public void shouldEditProductThroughAdmin() {
        String oldName = "Old_" + UUID.randomUUID();
        String newName = "Updated_" + UUID.randomUUID();

        AdminPage adminPage = loginAsAdmin();

        adminPage.addProduct(oldName, "120");

        PageAssert.assertNotificationVisible(
                adminPage.getNotification("Товар успешно добавлен!")
        );

        String productId = adminPage.getProductIdByName(oldName);

        adminPage.editProduct(productId, newName, "180");

        PageAssert.assertNotificationVisible(
                adminPage.getNotification(
                        "Товар #" + productId + " обновлен"
                )
        );

        adminPage.returnToStore();

        PageAssert.assertProductVisible(mainPage, newName);
        PageAssert.assertProductPrice(mainPage, newName, "180");
    }
}
