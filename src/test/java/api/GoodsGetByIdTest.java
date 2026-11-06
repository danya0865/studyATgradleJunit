package api;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

/** GET /goods/{id}: коды 200 и 404. */
@Tag("api")
public class GoodsGetByIdTest extends BaseApiTest {

    @Test
    void getExistingGood_returns200() {
        String name = GoodsApi.uniqueName();
        long id = createGood(name, 25.5);

        GoodsApi.get(id).then()
                .statusCode(200)
                .body("name", equalTo(name));
    }

    // 404: запрашиваем товар, который уже удалён
    @Test
    void getDeletedGood_returns404() {
        long id = createGood(GoodsApi.uniqueName(), 25.5);
        GoodsApi.delete(id).then().statusCode(200);

        GoodsApi.get(id).then()
                .statusCode(404);
    }

    // Найденный баг API: для ID, которого никогда не было, сервер отвечает 500 вместо 404
    @Disabled("БАГ API: GET /goods/{id} для несуществующего ID (например, 999999999) возвращает 500 вместо 404")
    @Test
    void getNonexistentGood_returns404() {
        GoodsApi.get(GoodsApi.NON_EXISTENT_ID).then()
                .statusCode(404);
    }
}