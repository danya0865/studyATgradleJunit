
package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;

public class AdminPage {

    private final SelenideElement newProductNameField = $("#n-name");
    private final SelenideElement newProductPriceField = $("#n-price");
    private final SelenideElement addProductButton = $("#add-btn");

    private final ElementsCollection productRows = $$("#tbody tr");
    private final ElementsCollection notifications = $$("#toast-container .toast");

    private final SelenideElement returnToStoreLink = $("a[href='/']");

    @Step("Ввести название нового товара: {name}")
    public AdminPage enterNewProductName(String name) {
        newProductNameField.setValue(name);
        return this;
    }

    @Step("Ввести цену нового товара: {price}")
    public AdminPage enterNewProductPrice(String price) {
        newProductPriceField.setValue(price);
        return this;
    }

    @Step("Нажать кнопку добавления товара")
    public AdminPage addProduct() {
        addProductButton.click();
        return this;
    }

    @Step("Добавить товар: {name}, цена {price}")
    public AdminPage addProduct(String name, String price) {
        enterNewProductName(name);
        enterNewProductPrice(price);
        addProduct();
        return this;
    }

    @Step("Изменить товар с ID {productId}: новое название {newName}, новая цена {newPrice}")
    public AdminPage editProduct(String productId, String newName, String newPrice) {
        $("#nm-" + productId).setValue(newName);
        $("#pr-" + productId).setValue(newPrice);

        $("button[data-action='update'][data-id='" + productId + "']").click();
        return this;
    }

    @Step("Изменить товар {oldName}: новое название {newName}, новая цена {newPrice}")
    public AdminPage editProductByName(String oldName, String newName, String newPrice) {
        String productId = getProductIdByName(oldName);
        return editProduct(productId, newName, newPrice);
    }

    @Step("Вернуться в магазин")
    public AdminPage returnToStore() {
        returnToStoreLink.click();
        return this;
    }

    // Getters for PageAssert: return element references without assertions

    public SelenideElement getNewProductNameField() {
        return newProductNameField;
    }

    public SelenideElement getNewProductPriceField() {
        return newProductPriceField;
    }

    public SelenideElement getAddProductButton() {
        return addProductButton;
    }

    public ElementsCollection getProductRows() {
        return productRows;
    }

    public SelenideElement getNotification(String text) {
        return notifications.findBy(
                com.codeborne.selenide.Condition.exactText(text)
        );
    }

    public SelenideElement getProductNameField(String productId) {
        return $("#nm-" + productId);
    }

    public SelenideElement getProductPriceField(String productId) {
        return $("#pr-" + productId);
    }

    @Step("Получить ID товара по названию: {productName}")
    public String getProductIdByName(String productName) {
        SelenideElement nameField = $$("tbody input[id^='nm-']")
                .findBy(com.codeborne.selenide.Condition.value(productName))
                .shouldBe(com.codeborne.selenide.Condition.visible);

        String fieldId = nameField.getAttribute("id");

        return fieldId.replace("nm-", "");
    }
}
