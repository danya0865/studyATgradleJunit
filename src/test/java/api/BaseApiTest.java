package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Базовый класс: запоминает созданные товары и удаляет их после каждого теста,
 * чтобы тесты не влияли друг на друга (например, тесты пустого списка).
 */
public abstract class BaseApiTest {

    private final List<Long> createdIds = new ArrayList<>();

    @BeforeAll
    static void enableLogging() {
        // при падении проверки REST Assured печатает запрос и ответ
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    protected Response addTracked(Map<String, Object> body) {
        Response response = GoodsApi.addRaw(body);
        if (response.statusCode() == 200) {
            createdIds.add(GoodsApi.extractId(response));
        }
        return response;
    }

    protected Response addTracked(String name, double price) {
        return addTracked(Map.of("name", name, "price", price));
    }

    /** Создаёт товар, проверяет код 200 и возвращает его ID. */
    protected long createGood(String name, double price) {
        Response response = addTracked(name, price);
        response.then().statusCode(200);
        return GoodsApi.extractId(response);
    }

    @AfterEach
    void deleteCreatedGoods() {
        for (Long id : createdIds) {
            GoodsApi.delete(id);
        }
        createdIds.clear();
    }
}