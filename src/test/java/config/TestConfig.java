
package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class TestConfig {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = TestConfig.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties не найден");
            }

            PROPERTIES.load(
                    new InputStreamReader(input, StandardCharsets.UTF_8)
            );

        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки конфигурации", e);
        }

        printConfig();
    }

    private TestConfig() {
    }

    public static String getAppUrl() {
        return PROPERTIES.getProperty("app.url");
    }

    public static String getApiUrl() {
        return PROPERTIES.getProperty("api.url");
    }

    public static int getTimeoutSeconds() {
        return Integer.parseInt(PROPERTIES.getProperty("timeout.seconds"));
    }

    public static String getLoggingMode() {
        return PROPERTIES.getProperty("logging.mode");
    }

    public static String getAdminUsername() {
        return PROPERTIES.getProperty("admin.username");
    }

    public static String getAdminPassword() {
        return PROPERTIES.getProperty("admin.password");
    }

    public static String getProductName() {
        return PROPERTIES.getProperty("product.name");
    }

    public static double getProductPrice() {
        return Double.parseDouble(PROPERTIES.getProperty("product.price"));
    }

    private static void printConfig() {
        System.out.println("========== TEST CONFIGURATION ==========");
        System.out.println("App URL: " + getAppUrl());
        System.out.println("API URL: " + getApiUrl());
        System.out.println("Timeout: " + getTimeoutSeconds() + " seconds");
        System.out.println("Logging mode: " + getLoggingMode());
        System.out.println("Product name: " + getProductName());
        System.out.println("Product price: " + getProductPrice());
        System.out.println("========================================");
    }
}
