
package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

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

    // Открыть главную страницу
    public MainPage openPage() {
        open("/");
        return this;
    }

    // Получить карточку товара по ID
    public SelenideElement getProductCard(long productId) {
        return $("#card-" + productId);
    }

    // Получить карточку товара по названию
    public SelenideElement getProductCard(String productName) {
        return productCards.findBy(text(productName));
    }

    // Установить количество товара
    public MainPage setProductQuantity(long productId, int quantity) {
        $("#q-" + productId).setValue(String.valueOf(quantity));
        return this;
    }

    // Добавить товар в корзину по ID
    public MainPage addProductToCart(long productId) {
        $("button[data-action='add-to-cart'][data-id='"
                + productId + "']").click();
        return this;
    }

    // Добавить товар в корзину по названию
    public MainPage addProductToCartByName(String productName) {
        SelenideElement productCard = getProductCard(productName)
                .shouldBe(visible);

        productCard
                .$("button[data-action='add-to-cart']")
                .click();

        return this;
    }

    // Открыть корзину
    public MainPage openCart() {
        openCartButton.click();
        return this;
    }

    // Изменить количество товара в корзине
    public MainPage changeCartQuantity(int index, int step) {
        $("[data-action='cart-qty'][data-index='" + index
                + "'][data-step='" + step + "']").click();
        return this;
    }

    // Удалить товар из корзины
    public MainPage removeProductFromCart(int index) {
        $("[data-action='remove'][data-index='" + index + "']").click();
        return this;
    }

    // Оформить заказ
    public MainPage makeOrder() {
        makeOrderButton.click();
        return this;
    }

    // Получить текст общей суммы
    public String getTotalPrice() {
        return totalPrice.getText();
    }

    // Получить количество товаров в корзине
    public String getCartCount() {
        return cartCount.getText();
    }

    // Получить цену товара по названию
    public String getProductPriceByName(String productName) {
        return getProductCard(productName).getAttribute("data-price");
    }

    // Получить уведомление по тексту
    public SelenideElement getNotification(String notificationText) {
        return notifications.findBy(
                com.codeborne.selenide.Condition.exactText(notificationText)
        );
    }

    // Получить текст заголовка
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
