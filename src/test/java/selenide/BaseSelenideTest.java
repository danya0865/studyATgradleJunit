
package selenide;

import com.codeborne.selenide.Configuration;
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
    }

    @AfterEach
    public void tearDown() {
        closeWebDriver();
    }
}
