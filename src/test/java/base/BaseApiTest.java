package base;

import constants.Urls;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;

public class BaseApiTest {

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = Urls.BASE_URI;

        RestAssured.filters(new AllureRestAssured());

        RestAssured.requestSpecification = RestAssured.given()
                .contentType(ContentType.JSON)
                .request();
    }
}
