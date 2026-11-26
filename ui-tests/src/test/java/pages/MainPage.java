
package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {

    // Заголовок страницы
    private final SelenideElement mainTitle = $("#main-title");

    // Список товаров и карточки
    private final SelenideElement productsList = $("#products-list");
    private final ElementsCollection productCards =
            $$("#products-list .product-card");

    // Корзина
    private final SelenideElement openCartButton = $("#open-cart-btn");
    private final SelenideElement cartCount = $("#cart-count");
    private final ElementsCollection cartItems =
            $$("#cart-items .cart-item");
    private final SelenideElement totalPrice = $("#total-price");
    private final SelenideElement makeOrderButton = $("#makeOrder");

    // Уведомления
    private final ElementsCollection notifications =
            $$("#toast-container .toast");

    @Step("Открыть главную страницу")
    public MainPage openPage() {
        open("/");
        return this;
    }

    @Step("Найти карточку товара с ID {productId}")
    public SelenideElement getProductCard(long productId) {
        return $("#card-" + productId);
    }

    @Step("Найти карточку товара {productName}")
    public SelenideElement getProductCard(String productName) {
        return productCards.findBy(text(productName));
    }

    @Step("Установить количество товара с ID {productId}: {quantity}")
    public MainPage setProductQuantity(long productId, int quantity) {
        $("#q-" + productId).setValue(String.valueOf(quantity));
        return this;
    }

    @Step("Добавить товар с ID {productId} в корзину")
    public MainPage addProductToCart(long productId) {
        $("button[data-action='add-to-cart'][data-id='" + productId + "']")
                .click();
        return this;
    }

    @Step("Добавить товар {productName} в корзину")
    public MainPage addProductToCartByName(String productName) {
        SelenideElement productCard = getProductCard(productName)
                .shouldBe(visible);

        productCard
                .$("button[data-action='add-to-cart']")
                .click();

        return this;
    }

    @Step("Открыть корзину")
    public MainPage openCart() {
        openCartButton.click();
        return this;
    }

    @Step("Изменить количество товара в корзине: индекс {index}, шаг {step}")
    public MainPage changeCartQuantity(int index, int step) {
        $("[data-action='cart-qty'][data-index='" + index
                + "'][data-step='" + step + "']").click();
        return this;
    }

    @Step("Удалить товар из корзины с индексом {index}")
    public MainPage removeProductFromCart(int index) {
        $("[data-action='remove'][data-index='" + index + "']").click();
        return this;
    }

    @Step("Оформить заказ")
    public MainPage makeOrder() {
        makeOrderButton.click();
        return this;
    }

    @Step("Получить общую сумму корзины")
    public String getTotalPrice() {
        return totalPrice.getText();
    }

    @Step("Получить количество товаров в корзине")
    public String getCartCount() {
        return cartCount.getText();
    }

    @Step("Получить цену товара {productName}")
    public String getProductPriceByName(String productName) {
        return getProductCard(productName).getAttribute("data-price");
    }

    @Step("Найти уведомление: {notificationText}")
    public SelenideElement getNotification(String notificationText) {
        return notifications.findBy(
                com.codeborne.selenide.Condition.exactText(notificationText)
        );
    }

    @Step("Получить заголовок главной страницы")
    public String getMainTitle() {
        return mainTitle.getText();
    }

    // Геттеры элементов для PageAssert

    public SelenideElement getMainTitleElement() {
        return mainTitle;
    }

    public SelenideElement getProductsList() {
        return productsList;
    }

    public SelenideElement getOpenCartButton() {
        return openCartButton;
    }

    public SelenideElement getCartCountElement() {
        return cartCount;
    }

    public SelenideElement getTotalPriceElement() {
        return totalPrice;
    }

    public SelenideElement getMakeOrderButton() {
        return makeOrderButton;
    }

    public ElementsCollection getCartItems() {
        return cartItems;
    }
}
