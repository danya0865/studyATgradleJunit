
package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AdminLoginPage {

    private final SelenideElement usernameField = $("[name='username']");
    private final SelenideElement passwordField = $("[name='password']");
    private final SelenideElement loginButton = $("button[type='submit']");

    @Step("Открыть страницу авторизации администратора")
    public AdminLoginPage openPage() {
        open("/admin");
        return this;
    }

    @Step("Ввести логин администратора")
    public AdminLoginPage enterUsername(String username) {
        usernameField.setValue(username);
        return this;
    }

    @Step("Ввести пароль администратора")
    public AdminLoginPage enterPassword(String password) {
        passwordField.setValue(password);
        return this;
    }

    @Step("Выполнить вход в панель администратора")
    public AdminPage login() {
        loginButton.click();
        return new AdminPage();
    }

    // Getters for PageAssert

    public SelenideElement getUsernameField() {
        return usernameField;
    }

    public SelenideElement getPasswordField() {
        return passwordField;
    }

    public SelenideElement getLoginButton() {
        return loginButton;
    }
}
