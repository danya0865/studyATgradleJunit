
package webinar;

import api.BaseApiTest;
import config.TestConfig;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Configuration.*;

public class BaseWebinarTest extends BaseApiTest {

    @BeforeEach
    public void setUp() {
        browser = "chrome";
        baseUrl = TestConfig.getAppUrl();
        browserSize = "1920x1080";
        timeout = TestConfig.getTimeoutSeconds() * 1000;
    }
}
