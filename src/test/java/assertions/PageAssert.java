
package assertions;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import pages.AdminLoginPage;
import pages.AdminPage;
import pages.MainPage;

import static com.codeborne.selenide.Condition.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PageAssert {

    // Проверка видимости элемента
    @Step("Проверить видимость элемента")
    public static void assertVisible(SelenideElement element) {
        element.shouldBe(visible);
    }

    // Проверка текста элемента
    @Step("Проверить текст элемента: ожидается {expectedText}")
    public static void assertText(SelenideElement element, String expectedText) {
        element.shouldHave(exactText(expectedText));
    }

    // Проверка значения поля ввода
    @Step("Проверить значение поля: ожидается {expectedValue}")
    public static void assertValue(SelenideElement element, String expectedValue) {
        element.shouldHave(value(expectedValue));
    }

    // Проверка, что коллекция содержит элементы
    @Step("Проверить, что коллекция не пустая")
    public static void assertCollectionNotEmpty(ElementsCollection elements) {
        assertThat(elements).isNotEmpty();
    }

    // Проверка основных элементов главной страницы
    @Step("Проверить загрузку главной страницы")
    public static void assertMainPageLoaded(MainPage page) {
        assertVisible(page.getMainTitleElement());
        assertVisible(page.getProductsList());
        assertVisible(page.getOpenCartButton());
        assertVisible(page.getCartCountElement());
    }

    // Проверка формы авторизации
    @Step("Проверить загрузку формы авторизации администратора")
    public static void assertAdminLoginFormLoaded(AdminLoginPage page) {
        assertVisible(page.getUsernameField());
        assertVisible(page.getPasswordField());
        assertVisible(page.getLoginButton());
    }

    // Проверка основных элементов страницы администратора
    @Step("Проверить загрузку страницы администратора")
    public static void assertAdminPageLoaded(AdminPage page) {
        assertVisible(page.getNewProductNameField());
        assertVisible(page.getNewProductPriceField());
        assertVisible(page.getAddProductButton());
    }

    // Проверка элементов корзины после открытия
    @Step("Проверить, что корзина открыта")
    public static void assertCartOpened(MainPage page) {
        assertVisible(page.getTotalPriceElement());
        assertVisible(page.getMakeOrderButton());
    }

    // Проверка уведомления
    @Step("Проверить видимость уведомления")
    public static void assertNotificationVisible(SelenideElement notification) {
        assertVisible(notification);
    }

    // Проверка цены товара в каталоге
    @Step("Проверить цену товара {productName}: ожидается {expectedPrice}")
    public static void assertProductPrice(
            MainPage page,
            String productName,
            String expectedPrice
    ) {
        assertThat(page.getProductPriceByName(productName))
                .isEqualTo(expectedPrice);
    }

    // Проверка количества товаров в корзине
    @Step("Проверить количество товаров в корзине: ожидается {expectedCount}")
    public static void assertCartCount(MainPage page, String expectedCount) {
        assertThat(page.getCartCount()).isEqualTo(expectedCount);
    }

    // Проверка общей суммы корзины
    @Step("Проверить общую сумму корзины: ожидается {expectedPrice}")
    public static void assertTotalPrice(MainPage page, String expectedPrice) {
        assertThat(page.getTotalPrice()).isEqualTo(expectedPrice);
    }

    // Проверка наличия товара в каталоге
    @Step("Проверить наличие товара в каталоге: {productName}")
    public static void assertProductVisible(MainPage page, String productName) {
        assertVisible(page.getProductCard(productName));
    }

    // Проверка заголовка главной страницы
    @Step("Проверить заголовок главной страницы: {expectedTitle}")
    public static void assertMainTitle(MainPage page, String expectedTitle) {
        assertThat(page.getMainTitle()).isEqualTo(expectedTitle);
    }

    // Проверка названия и цены товара в админке
    @Step("Проверить название и цену товара с ID {productId}")
    public static void assertProductFieldValue(
            AdminPage page,
            String productId,
            String expectedName,
            String expectedPrice
    ) {
        assertValue(page.getProductNameField(productId), expectedName);
        assertValue(page.getProductPriceField(productId), expectedPrice);
    }
}
