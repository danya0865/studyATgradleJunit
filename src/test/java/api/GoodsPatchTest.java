package api;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

/** PATCH /goods/{id}: коды 200, 400 и 404. */
@Tag("api")
public class GoodsPatchTest extends BaseApiTest {

    @Test
    void patchExistingGood_returns200() {
        long id = createGood(GoodsApi.uniqueName(), 10.5);
        String newName = GoodsApi.uniqueName();

        GoodsApi.patch(id, Map.of("name", newName, "price", 20.5)).then()
                .statusCode(200);

        // изменения применились
        GoodsApi.get(id).then()
                .statusCode(200)
                .body("name", equalTo(newName));
    }

    @Test
    void patchWithDuplicateName_returns400() {
        String takenName = GoodsApi.uniqueName();
        createGood(takenName, 10.5);
        long id = createGood(GoodsApi.uniqueName(), 12.5);

        // имя уже занято другим товаром
        GoodsApi.patch(id, Map.of("name", takenName)).then()
                .statusCode(400);
    }

    @Test
    void patchNonexistentGood_returns404() {
        GoodsApi.patch(GoodsApi.NON_EXISTENT_ID, Map.of("name", GoodsApi.uniqueName())).then()
                .statusCode(404);
    }
}