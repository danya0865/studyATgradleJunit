package webinar;

import api.BaseApiTest;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Configuration.*;

public class BaseWebinarTest extends BaseApiTest {

    @BeforeEach
    public void setUp() {
        browser = "chrome";
        baseUrl = "http://localhost:8080";
        browserSize = "1920x1080";
    }
}