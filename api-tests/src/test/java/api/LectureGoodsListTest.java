
package api;


import basicApi.GoodsApi;
import assertions.ApiAssert;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

/**
 * Р›РµРєС†РёСЏ, Р·Р°РґР°С‡Р° 1: С‡РµС‚С‹СЂРµ С‚РµСЃС‚Р° РЅР° GET /goods/list.
 */
@Tag("api")
public class LectureGoodsListTest extends BaseApiTest {

    // 1. given() / when(): РїСЂРѕРІРµСЂСЏРµРј РєРѕРґ РѕС‚РІРµС‚Р° Рё СЃС‚СЂСѓРєС‚СѓСЂСѓ СЃРїРёСЃРєР°
    @Test
    void listWithGivenWhenThen() {
        Response response = given()
                .spec(GoodsApi.SPEC)
                .when()
                .get("/goods/list");

        ApiAssert.assertStatusCode(response, 200);
        ApiAssert.assertFieldIsList(response, "goods");
    }

    // 2. RequestSpecification: РїСЂРѕРІРµСЂСЏРµРј РєРѕРґ РѕС‚РІРµС‚Р° Рё СЃС‚СЂСѓРєС‚СѓСЂСѓ СЃРїРёСЃРєР°
    @Test
    void listWithRequestSpecification() {
        RequestSpecification spec = new RequestSpecBuilder()
                .setBaseUri(GoodsApi.BASE_URI)
                .addFilter(new AllureRestAssured())
                .build();

        Response response = given()
                .spec(spec)
                .when()
                .get("/goods/list");

        ApiAssert.assertStatusCode(response, 200);
        ApiAssert.assertFieldIsList(response, "goods");
    }

    // 3. РЎРѕР·РґР°С‘Рј С‚РѕРІР°СЂ Рё РїСЂРѕРІРµСЂСЏРµРј РµРіРѕ РЅР°Р»РёС‡РёРµ РІ СЃРїРёСЃРєРµ
    @Test
    void createdGoodIsInListBuiltInChecks() {
        String name = GoodsApi.uniqueName();
        createGood(name, 100.5);

        Response response = GoodsApi.list(0, 100);

        ApiAssert.assertStatusCode(response, 200);
        ApiAssert.assertListContains(response, "goods.name", name);
    }

    // 4. РџСЂРѕРІРµСЂСЏРµРј СЃРїРёСЃРѕРє С‡РµСЂРµР· AssertJ
    @Test
    void createdGoodIsInListAssertJ() {
        String name = GoodsApi.uniqueName();
        createGood(name, 100.5);

        Response response = GoodsApi.list(0, 100);

        ApiAssert.assertStatusCode(response, 200);
        ApiAssert.assertListContains(response, "goods.name", name);
    }
}

