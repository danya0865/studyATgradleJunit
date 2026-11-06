package api;

import io.restassured.response.Response;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/** POST /goods/add: коды 200 и 400. */
@Tag("api")
public class GoodsAddTest extends BaseApiTest {

    @Test
    void addValidGood_returns200() {
        Response response = addTracked(GoodsApi.uniqueName(), 10.5);

        assertThat(response.statusCode())
                .as("POST /goods/add с корректным телом")
                .isEqualTo(200);
    }

    @Test
    void addGoodWithNegativePrice_returns400() {
        Response response = addTracked(GoodsApi.uniqueName(), -1.0);

        assertThat(response.statusCode())
                .as("POST /goods/add с отрицательной ценой")
                .isEqualTo(400);
    }

    @Test
    void addGoodWithoutName_returns400() {
        Response response = addTracked(Map.of("price", 10.5));

        assertThat(response.statusCode())
                .as("POST /goods/add без имени товара")
                .isEqualTo(400);
    }
}