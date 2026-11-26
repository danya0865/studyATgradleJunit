
package webinar;

import api.BaseApiTest;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.TestConfig;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import static com.codeborne.selenide.Configuration.*;
import static com.codeborne.selenide.Selenide.closeWebDriver;

public class BaseWebinarTest extends BaseApiTest {

    @BeforeEach
    public void setUp() {
        browser = "chrome";
        baseUrl = TestConfig.getAppUrl();
        browserSize = "1920x1080";
        timeout = TestConfig.getTimeoutSeconds() * 1000;

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
