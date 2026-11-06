package api;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** DELETE /goods/{id}: коды 200 и 404. */
@Tag("api")
public class GoodsDeleteTest extends BaseApiTest {

    @Test
    void deleteExistingGood_returns200() {
        long id = createGood(GoodsApi.uniqueName(), 15.5);

        GoodsApi.delete(id).then()
                .statusCode(200);

        // товар действительно удалён
        GoodsApi.get(id).then()
                .statusCode(404);
    }

    @Test
    void deleteNonexistentGood_returns404() {
        GoodsApi.delete(GoodsApi.NON_EXISTENT_ID).then()
                .statusCode(404);
    }
}