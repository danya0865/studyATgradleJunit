
package api;

import assertions.ApiAssert;
import basicApi.GoodsApi;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Базовый класс: запоминает созданные товары и удаляет их после каждого теста.
 */
public abstract class BaseApiTest {

    private final List<Long> createdIds = new ArrayList<>();

    @BeforeAll
    static void enableLogging() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Step("Добавить товар и отслеживать его ID")
    protected Response addTracked(Map<String, Object> body) {
        Response response = GoodsApi.addRaw(body);
        if (response.statusCode() == 200) {
            createdIds.add(GoodsApi.extractId(response));
        }
        return response;
    }

    @Step("Добавить товар {name} с ценой {price}")
    protected Response addTracked(String name, double price) {
        return addTracked(Map.of("name", name, "price", price));
    }

    @Step("Создать товар {name} с ценой {price}")
    protected long createGood(String name, double price) {
        Response response = addTracked(name, price);
        ApiAssert.assertStatusCode(response, 200);
        return GoodsApi.extractId(response);
    }

    @Step("Удалить созданные в тесте товары")
    @AfterEach
    void deleteCreatedGoods() {
        for (Long id : createdIds) {
            GoodsApi.delete(id);
        }
        createdIds.clear();
    }
}
