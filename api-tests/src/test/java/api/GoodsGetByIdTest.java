
package api;


import basicApi.GoodsApi;
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

    // 404: Р·Р°РїСЂР°С€РёРІР°РµРј С‚РѕРІР°СЂ, РєРѕС‚РѕСЂС‹Р№ СѓР¶Рµ СѓРґР°Р»С‘РЅ
    @Test
    void getDeletedGood_returns404() {
        long id = createGood(GoodsApi.uniqueName(), 25.5);

        ApiAssert.assertStatusCode(GoodsApi.delete(id), 200);
        ApiAssert.assertStatusCode(GoodsApi.get(id), 404);
    }

    // РќР°Р№РґРµРЅРЅС‹Р№ Р±Р°Рі API: РґР»СЏ РЅРµСЃСѓС‰РµСЃС‚РІСѓСЋС‰РµРіРѕ ID СЃРµСЂРІРµСЂ РѕС‚РІРµС‡Р°РµС‚ 500 РІРјРµСЃС‚Рѕ 404
    @Disabled("Р‘РђР“ API: GET /goods/{id} РґР»СЏ РЅРµСЃСѓС‰РµСЃС‚РІСѓСЋС‰РµРіРѕ ID РІРѕР·РІСЂР°С‰Р°РµС‚ 500 РІРјРµСЃС‚Рѕ 404")
    @Test
    void getNonexistentGood_returns404() {
        ApiAssert.assertStatusCode(
                GoodsApi.get(GoodsApi.NON_EXISTENT_ID),
                404
        );
    }
}

