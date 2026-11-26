
package api;

import assertions.ApiAssert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("api")
public class GoodsDeleteTest extends BaseApiTest {

    @Test
    void deleteExistingGood_returns200() {
        long id = createGood(GoodsApi.uniqueName(), 15.5);

        ApiAssert.assertStatusCode(GoodsApi.delete(id), 200);

        // Проверяем, что товар действительно удалён
        ApiAssert.assertStatusCode(GoodsApi.get(id), 404);
    }

    @Test
    void deleteNonexistentGood_returns404() {
        ApiAssert.assertStatusCode(
                GoodsApi.delete(GoodsApi.NON_EXISTENT_ID),
                404
        );
    }
}
