
package config;

import config.TestConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class RestApiBuilder {

    private RestApiBuilder() {
    }

    public static RequestSpecification build() {
        return new RequestSpecBuilder()
                .setBaseUri(TestConfig.getApiUrl())
                .setContentType(ContentType.JSON)
                .setAuth(RestAssured.preemptive().basic(
                        TestConfig.getAdminUsername(),
                        TestConfig.getAdminPassword()
                ))
                .addFilter(new AllureRestAssured())
                .build();
    }
}
