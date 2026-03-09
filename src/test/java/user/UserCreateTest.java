package user;

import base.UserBaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;

import static java.net.HttpURLConnection.*;
import static testdata.UserGenerator.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static testdata.UserGenerator.credsFrom;

public class UserCreateTest extends UserBaseTest {

    @After
    public void tearDown() {
        // если пользователь создавался, но accessToken ещё не получили — логинимся здесь
        if (createdUser != null && accessToken == null) {
            accessToken = userSteps.login(credsFrom(createdUser))
                    .then()
                    .extract()
                    .path("accessToken");
        }

        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя успешно")
    @Description("Создаём нового пользователя и проверяем success=true и наличие токенов")
    public void shouldCreateUniqueUser() {
        createdUser = randomUser();

        userSteps.register(createdUser)
                .then()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());

        // login для cleanup НЕ делаем в тесте — он в @After
    }

    @Test
    @DisplayName("Нельзя создать уже зарегистрированного пользователя")
    @Description("Регистрируем пользователя, затем повторяем регистрацию и ожидаем ошибку")
    public void shouldNotCreateAlreadyRegisteredUser() {
        createdUser = randomUser();
        accessToken = userSteps.register(createdUser)
                .then()
                .extract()
                .path("accessToken");

        userSteps.register(createdUser)
                .then()
                .statusCode(HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Нельзя создать пользователя без name")
    @Description("Отправляем запрос без name и ожидаем ошибку обязательных полей")
    public void shouldNotCreateUserWithoutName() {
        createdUser = withoutName();

        userSteps.register(createdUser)
                .then()
                .statusCode(HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Нельзя создать пользователя без email")
    @Description("Отправляем запрос без email и ожидаем ошибку обязательных полей")
    public void shouldNotCreateUserWithoutEmail() {
        createdUser = withoutEmail();

        userSteps.register(createdUser)
                .then()
                .statusCode(HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Нельзя создать пользователя без password")
    @Description("Отправляем запрос без password и ожидаем ошибку обязательных полей")
    public void shouldNotCreateUserWithoutPassword() {
        createdUser = withoutPassword();

        userSteps.register(createdUser)
                .then()
                .statusCode(HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}