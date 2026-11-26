
package api;


import basicApi.GoodsApi;
import assertions.ApiAssert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Tag("api")
public class GoodsPatchTest extends BaseApiTest {

    @Test
    void patchExistingGood_returns200() {
        long id = createGood(GoodsApi.uniqueName(), 10.5);
        String newName = GoodsApi.uniqueName();

        ApiAssert.assertStatusCode(
                GoodsApi.patch(id, Map.of("name", newName, "price", 20.5)),
                200
        );

        // РџСЂРѕРІРµСЂСЏРµРј, С‡С‚Рѕ РёР·РјРµРЅРµРЅРёСЏ РїСЂРёРјРµРЅРёР»РёСЃСЊ
        var response = GoodsApi.get(id);

        ApiAssert.assertStatusCode(response, 200);
        ApiAssert.assertBodyFieldEquals(response, "name", newName);
    }

    @Test
    void patchWithDuplicateName_returns400() {
        String takenName = GoodsApi.uniqueName();

        createGood(takenName, 10.5);
        long id = createGood(GoodsApi.uniqueName(), 12.5);

        // РРјСЏ СѓР¶Рµ Р·Р°РЅСЏС‚Рѕ РґСЂСѓРіРёРј С‚РѕРІР°СЂРѕРј
        ApiAssert.assertStatusCode(
                GoodsApi.patch(id, Map.of("name", takenName)),
                400
        );
    }

    @Test
    void patchNonexistentGood_returns404() {
        ApiAssert.assertStatusCode(
                GoodsApi.patch(
                        GoodsApi.NON_EXISTENT_ID,
                        Map.of("name", GoodsApi.uniqueName())
                ),
                404
        );
    }
}

