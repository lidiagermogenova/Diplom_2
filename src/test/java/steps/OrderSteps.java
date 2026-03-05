package steps;

import constants.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.order.OrderCreate;
import static io.restassured.RestAssured.given;

public class OrderSteps {
    @Step("Получить ингредиенты")
    public Response getIngredients() {
        return given()
                .when()
                .get(Endpoints.INGREDIENTS);
    }

    @Step("Создать заказ без авторизации")
    public Response createOrderNoAuth(OrderCreate order) {
        return given()
                .body(order)
                .when()
                .post(Endpoints.CREATE_ORDER);
    }

    @Step("Создать заказ с авторизацией")
    public Response createOrderAuth(String accessToken, OrderCreate order) {
        return given()
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(Endpoints.CREATE_ORDER);
    }
}
