
package api;

import assertions.ApiAssert;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

/**
 * Лекция, задача 1: четыре теста на GET /goods/list.
 */
@Tag("api")
public class LectureGoodsListTest extends BaseApiTest {

    // 1. given() / when(): проверяем код ответа и структуру списка
    @Test
    void listWithGivenWhenThen() {
        Response response = given()
                .spec(GoodsApi.SPEC)
                .when()
                .get("/goods/list");

        ApiAssert.assertStatusCode(response, 200);
        ApiAssert.assertFieldIsList(response, "goods");
    }

    // 2. RequestSpecification: проверяем код ответа и структуру списка
    @Test
    void listWithRequestSpecification() {
        RequestSpecification spec = new RequestSpecBuilder()
                .setBaseUri(GoodsApi.BASE_URI)
                .addFilter(new AllureRestAssured())
                .build();

        Response response = given()
                .spec(spec)
                .when()
                .get("/goods/list");

        ApiAssert.assertStatusCode(response, 200);
        ApiAssert.assertFieldIsList(response, "goods");
    }

    // 3. Создаём товар и проверяем его наличие в списке
    @Test
    void createdGoodIsInListBuiltInChecks() {
        String name = GoodsApi.uniqueName();
        createGood(name, 100.5);

        Response response = GoodsApi.list(0, 100);

        ApiAssert.assertStatusCode(response, 200);
        ApiAssert.assertListContains(response, "goods.name", name);
    }

    // 4. Проверяем список через AssertJ
    @Test
    void createdGoodIsInListAssertJ() {
        String name = GoodsApi.uniqueName();
        createGood(name, 100.5);

        Response response = GoodsApi.list(0, 100);

        ApiAssert.assertStatusCode(response, 200);
        ApiAssert.assertListContains(response, "goods.name", name);
    }
}
