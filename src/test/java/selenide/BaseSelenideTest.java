
package selenide;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class BaseSelenideTest {

    @BeforeEach
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.baseUrl = TestConfig.getAppUrl();
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = TestConfig.getTimeoutSeconds() * 1000;

        // Подключаем Allure Selenide Listener
        SelenideLogger.addListener(
                "AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(true)
        );
    }

    @AfterEach
    public void tearDown() {
        closeWebDriver();
        SelenideLogger.removeListener("AllureSelenide");
    }
}
