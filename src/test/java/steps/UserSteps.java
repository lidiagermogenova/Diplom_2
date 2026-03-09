package steps;

import constants.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.user.UserCreate;
import model.user.UserLoginRequest;
import static io.restassured.RestAssured.given;

public class UserSteps {
    @Step("Регистрация пользователя")
    public Response register(UserCreate user) {
        return given()
                .body(user)
                .when()
                .post(Endpoints.USER_REGISTER);
    }

    @Step("Логин пользователя")
    public Response login(UserLoginRequest creds) {
        return given()
                .body(creds)
                .when()
                .post(Endpoints.USER_LOGIN);
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(Endpoints.USER_DELETE);
    }
}
