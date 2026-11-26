package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AdminLoginPage {

    private final SelenideElement usernameField = $("[name='username']");
    private final SelenideElement passwordField = $("[name='password']");
    private final SelenideElement loginButton = $("button[type='submit']");

    public AdminLoginPage openPage() {
        open("/admin");
        return this;
    }

    public AdminLoginPage enterUsername(String username) {
        usernameField.setValue(username);
        return this;
    }

    public AdminLoginPage enterPassword(String password) {
        passwordField.setValue(password);
        return this;
    }

    public AdminPage login() {
        loginButton.click();
        return new AdminPage();
    }

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