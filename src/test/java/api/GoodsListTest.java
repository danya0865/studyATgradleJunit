package api;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;

/** GET /goods/list: код 200 и пагинация. */
@Tag("api")
public class GoodsListTest extends BaseApiTest {

    @Test
    void listGoods_returns200() {
        GoodsApi.list().then()
                .statusCode(200);
    }

    @Test
    void listRespectsPageSize() {
        createGood(GoodsApi.uniqueName(), 10.5);
        createGood(GoodsApi.uniqueName(), 11.5);
        createGood(GoodsApi.uniqueName(), 12.5);

        GoodsApi.list(0, 2).then()
                .statusCode(200)
                .body("goods.size()", is(2));
    }
}