
package api;

import assertions.ApiAssert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("api")
public class GoodsGetByIdTest extends BaseApiTest {

    @Test
    void getExistingGood_returns200() {
        String name = GoodsApi.uniqueName();
        long id = createGood(name, 25.5);

        var response = GoodsApi.get(id);

        ApiAssert.assertStatusCode(response, 200);
        ApiAssert.assertBodyFieldEquals(response, "name", name);
    }

    // 404: запрашиваем товар, который уже удалён
    @Test
    void getDeletedGood_returns404() {
        long id = createGood(GoodsApi.uniqueName(), 25.5);

        ApiAssert.assertStatusCode(GoodsApi.delete(id), 200);
        ApiAssert.assertStatusCode(GoodsApi.get(id), 404);
    }

    // Найденный баг API: для несуществующего ID сервер отвечает 500 вместо 404
    @Disabled("БАГ API: GET /goods/{id} для несуществующего ID возвращает 500 вместо 404")
    @Test
    void getNonexistentGood_returns404() {
        ApiAssert.assertStatusCode(
                GoodsApi.get(GoodsApi.NON_EXISTENT_ID),
                404
        );
    }
}
