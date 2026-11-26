
package api;

import assertions.ApiAssert;
import io.restassured.response.Response;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("api")
public class GoodsListTest extends BaseApiTest {

    @Test
    void listGoods_returns200() {
        Response response = GoodsApi.list();

        ApiAssert.assertStatusCode(response, 200);
    }

    @Test
    void listRespectsPageSize() {
        createGood(GoodsApi.uniqueName(), 10.5);
        createGood(GoodsApi.uniqueName(), 11.5);
        createGood(GoodsApi.uniqueName(), 12.5);

        Response response = GoodsApi.list(0, 2);

        ApiAssert.assertStatusCode(response, 200);
        ApiAssert.assertListSize(response, "goods", 2);
    }
}
