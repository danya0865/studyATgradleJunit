
package base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.TestConfig;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public abstract class BaseUiTest {

    private static final String ALLURE_LISTENER = "AllureSelenide";

    @BeforeEach
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.baseUrl = TestConfig.getAppUrl();
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = TestConfig.getTimeoutSeconds() * 1000;

        SelenideLogger.addListener(
                ALLURE_LISTENER,
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(true)
        );
    }

    @AfterEach
    public void tearDown() {
        closeWebDriver();
        SelenideLogger.removeListener(ALLURE_LISTENER);
    }
}
