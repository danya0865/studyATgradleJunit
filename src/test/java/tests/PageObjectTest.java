
package tests;

import assertions.PageAssert;
import config.TestConfig;
import org.junit.jupiter.api.Test;
import pages.AdminLoginPage;
import pages.AdminPage;
import pages.MainPage;
import selenide.BaseSelenideTest;

import java.util.UUID;

public class PageObjectTest extends BaseSelenideTest {

    private final MainPage mainPage = new MainPage();
    private final AdminLoginPage adminLoginPage = new AdminLoginPage();

    // Авторизация администратора
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

    // 2.1. Добавление трёх единиц товара и оформление заказа
    @Test
    public void shouldCreateOrderWithThreeItems() {
        String productName = "PageObject_" + UUID.randomUUID();
        String productPrice = "90";

        // Добавляем товар через админку
        AdminPage adminPage = loginAsAdmin();
        adminPage.addProduct(productName, productPrice);

        PageAssert.assertNotificationVisible(
                adminPage.getNotification("Товар успешно добавлен!")
        );

        // Переходим в каталог и добавляем товар три раза
        adminPage.returnToStore();

        PageAssert.assertMainPageLoaded(mainPage);

        mainPage.addProductToCartByName(productName);
        mainPage.addProductToCartByName(productName);
        mainPage.addProductToCartByName(productName);

        // Проверяем корзину
        mainPage.openCart();

        PageAssert.assertCartOpened(mainPage);
        PageAssert.assertTotalPrice(mainPage, "270");

        // Оформляем заказ
        mainPage.makeOrder();

        PageAssert.assertNotificationVisible(
                mainPage.getNotification("Заказ принят в обработку!")
        );
    }

    // 2.2. Добавление разных товаров и проверка общей стоимости
    @Test
    public void shouldCalculateTotalPriceForDifferentProducts() {
        String firstProduct = "First_" + UUID.randomUUID();
        String secondProduct = "Second_" + UUID.randomUUID();

        // Добавляем два товара через админку
        AdminPage adminPage = loginAsAdmin();

        adminPage.addProduct(firstProduct, "100");

        PageAssert.assertNotificationVisible(
                adminPage.getNotification("Товар успешно добавлен!")
        );

        adminPage.addProduct(secondProduct, "150");

        PageAssert.assertNotificationVisible(
                adminPage.getNotification("Товар успешно добавлен!")
        );

        // Переходим в каталог
        adminPage.returnToStore();

        PageAssert.assertMainPageLoaded(mainPage);

        // Добавляем разные товары в корзину
        mainPage.addProductToCartByName(firstProduct);
        mainPage.addProductToCartByName(secondProduct);

        // Проверяем итоговую сумму
        mainPage.openCart();

        PageAssert.assertCartOpened(mainPage);
        PageAssert.assertTotalPrice(mainPage, "250");
    }

    // 2.3. Авторизация и добавление товара через админку
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

    // 2.4. Авторизация, редактирование товара и проверка изменений
    @Test
    public void shouldEditProductThroughAdmin() {
        String oldName = "Old_" + UUID.randomUUID();
        String newName = "Updated_" + UUID.randomUUID();

        AdminPage adminPage = loginAsAdmin();

        // Создаём товар
        adminPage.addProduct(oldName, "120");

        PageAssert.assertNotificationVisible(
                adminPage.getNotification("Товар успешно добавлен!")
        );

        // Получаем ID созданного товара
        String productId = adminPage.getProductIdByName(oldName);

        // Редактируем товар
        adminPage.editProduct(productId, newName, "180");

        PageAssert.assertNotificationVisible(
                adminPage.getNotification(
                        "Товар #" + productId + " обновлен"
                )
        );

        // Возвращаемся в каталог
        adminPage.returnToStore();

        // Проверяем новое название и цену
        PageAssert.assertProductVisible(mainPage, newName);
        PageAssert.assertProductPrice(mainPage, newName, "180");
    }
}
