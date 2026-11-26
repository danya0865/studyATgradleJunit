
package api;


import basicApi.GoodsApi;
import assertions.ApiAssert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Tag("api")
public class GoodsAddTest extends BaseApiTest {

    @Test
    void addValidGood_returns200() {
        var response = addTracked(GoodsApi.uniqueName(), 10.5);

        ApiAssert.assertStatusCode(response, 200);
    }

    @Test
    void addGoodWithNegativePrice_returns400() {
        var response = addTracked(GoodsApi.uniqueName(), -1.0);

        ApiAssert.assertStatusCode(response, 400);
    }

    @Test
    void addGoodWithoutName_returns400() {
        var response = addTracked(Map.of("price", 10.5));

        ApiAssert.assertStatusCode(response, 400);
    }
}

