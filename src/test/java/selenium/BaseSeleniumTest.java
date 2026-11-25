
package selenium;

import config.TestConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class BaseSeleniumTest {

    protected WebDriver driver;

    protected String baseUrl = TestConfig.getAppUrl();

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);

        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(TestConfig.getTimeoutSeconds())
        );
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
