package api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;

/**
 * Лекция, задача 1: четыре теста на GET /goods/list.
 * Ответ имеет вид {"goods": [...]}. Тесты 1 и 2 ожидают пустой список товаров
 * (поле goods пустое), поэтому сервер перед прогоном лучше перезапустить.
 */
@Tag("api")
public class LectureGoodsListTest extends BaseApiTest {

    // 1. given() / when() / then(): код ответа и пустое тело
    @Test
    void listWithGivenWhenThen() {
        given()
                .baseUri(GoodsApi.BASE_URI)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods", empty());
    }

    // 2. RequestSpecification: код ответа и пустое тело
    @Test
    void listWithRequestSpecification() {
        RequestSpecification spec = new RequestSpecBuilder()
                .setBaseUri(GoodsApi.BASE_URI)
                .build();

        given().spec(spec)
                .when().get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods", empty());
    }

    // 3. Создаём товар через POST /goods/add, проверяем GET /goods/list встроенными проверками REST Assured
    @Test
    void createdGoodIsInListBuiltInChecks() {
        String name = GoodsApi.uniqueName();
        createGood(name, 100.5);

        given().spec(GoodsApi.SPEC)
                .when().get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods.name", hasItem(name));
    }

    // 4. То же самое, но проверка через AssertJ
    @Test
    void createdGoodIsInListAssertJ() {
        String name = GoodsApi.uniqueName();
        createGood(name, 100.5);

        Response response = GoodsApi.list();
        List<String> names = response.jsonPath().getList("goods.name", String.class);

        assertThat(response.statusCode()).as("код ответа GET /goods/list").isEqualTo(200);
        assertThat(names).as("названия товаров в ответе GET /goods/list").contains(name);
    }
}