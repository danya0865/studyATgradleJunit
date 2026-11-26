package api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import config.TestConfig;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;

import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;

/**
 * Обёртка над эндпоинтами учебного API, чтобы не дублировать запросы в тестах.
 */
public final class GoodsApi {

    public static final String BASE_URI = TestConfig.getApiUrl();

    public static final String USERNAME = TestConfig.getAdminUsername();
    public static final String PASSWORD = TestConfig.getAdminPassword();

    // ID, которого заведомо нет в системе
    public static final long NON_EXISTENT_ID = 999_999_999L;

    public static final RequestSpecification SPEC = new RequestSpecBuilder()
            .setBaseUri(BASE_URI)
            .setContentType(ContentType.JSON)
            .setAuth(RestAssured.preemptive().basic(USERNAME, PASSWORD))
            .addFilter(new AllureRestAssured())
            .build();

    private GoodsApi() {
    }

    @Step("Сгенерировать уникальное имя товара")
    /** Уникальное имя товара (имена в API должны быть уникальными). */
    public static String uniqueName() {
        return "good-" + UUID.randomUUID();
    }

    // POST /goods/add
    @Step("Добавить товар {name} с ценой {price}")
    public static Response add(String name, double price) {
        return addRaw(Map.of("name", name, "price", price));
    }

    @Step("Отправить POST /goods/add")
    public static Response addRaw(Map<String, Object> body) {
        return given().spec(SPEC)
                .body(body)
                .when().post("/goods/add");
    }

    @Step("Получить ID созданного товара")
    /** Достаёт ID созданного товара из ответа POST /goods/add (поле data). */
    public static long extractId(Response response) {
        Map<String, Number> data = response.jsonPath().getMap("data");
        return data.values().iterator().next().longValue();
    }

    @Step("Получить товар с ID {id}")
    // GET /goods/{id}
    public static Response get(long id) {
        return given().spec(SPEC)
                .when().get("/goods/{id}", id);
    }

    @Step("Удалить товар с ID {id}")
    // DELETE /goods/{id}
    public static Response delete(long id) {
        return given().spec(SPEC)
                .when().delete("/goods/{id}", id);
    }

    @Step("Изменить товар с ID {id}")
    // PATCH /goods/{id}
    public static Response patch(long id, Map<String, Object> body) {
        return given().spec(SPEC)
                .body(body)
                .when().patch("/goods/{id}", id);
    }

    @Step("Получить список товаров")
    // GET /goods/list
    public static Response list() {
        return given().spec(SPEC)
                .when().get("/goods/list");
    }

    @Step("Получить список товаров: страница {page}, размер {size}")
    public static Response list(int page, int size) {
        return given().spec(SPEC)
                .queryParam("page", page)
                .queryParam("size", size)
                .when().get("/goods/list");
    }
}