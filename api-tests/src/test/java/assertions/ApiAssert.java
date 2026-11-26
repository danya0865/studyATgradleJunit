
package assertions;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiAssert {

    @Step("Проверить HTTP-статус ответа: ожидается {expectedStatus}")
    public static void assertStatusCode(Response response, int expectedStatus) {
        assertThat(response.statusCode())
                .as("HTTP-статус ответа")
                .isEqualTo(expectedStatus);
    }

    @Step("Проверить, что поле {field} содержит список")
    public static void assertFieldIsList(Response response, String field) {
        Object actualValue = response.jsonPath().get(field);

        assertThat(actualValue)
                .as("Поле " + field + " должно быть списком")
                .isInstanceOf(List.class);
    }

    @Step("Проверить поле ответа {field} — ожидается {expectedValue}")
    public static void assertBodyFieldEquals(
            Response response, String field, Object expectedValue) {
        Object actualValue = response.jsonPath().get(field);

        assertThat(actualValue)
                .as("Значение поля " + field)
                .isEqualTo(expectedValue);
    }

    @Step("Проверить количество элементов: ожидается {expectedSize}")
    public static void assertListSize(
            Response response, String jsonPath, int expectedSize) {
        int actualSize = response.jsonPath().getList(jsonPath).size();

        assertThat(actualSize)
                .as("Количество элементов по пути " + jsonPath)
                .isEqualTo(expectedSize);
    }

    @Step("Проверить наличие элемента {expectedValue} в списке {jsonPath}")
    public static void assertListContains(
            Response response, String jsonPath, String expectedValue) {
        List<String> values = response.jsonPath()
                .getList(jsonPath, String.class);

        assertThat(values)
                .as("Содержимое списка " + jsonPath)
                .contains(expectedValue);
    }
}
