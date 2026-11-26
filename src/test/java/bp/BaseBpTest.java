
package bp;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.TestConfig;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class BaseBpTest {

    @BeforeEach
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.baseUrl = TestConfig.getAppUrl();
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = TestConfig.getTimeoutSeconds() * 1000;

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
