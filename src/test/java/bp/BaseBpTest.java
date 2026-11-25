package bp;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class BaseBpTest {

    @BeforeEach
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.baseUrl = "http://localhost:8080";
        Configuration.browserSize = "1920x1080";
    }

    @AfterEach
    public void tearDown() {
        closeWebDriver();
    }
}