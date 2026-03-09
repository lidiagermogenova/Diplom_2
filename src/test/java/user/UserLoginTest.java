package user;

import base.UserBaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.user.UserLoginRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static testdata.UserGenerator.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserLoginTest extends UserBaseTest {

    /*@Before
    public void setUp() {
        // Пользователь - создаём в @Before
        createdUser = randomUser();
        accessToken = userSteps.register(createdUser)
                .then()
                .extract()
                .path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }*/

    @Test
    @DisplayName("Логин существующего пользователя успешен")
    @Description("Создаём пользователя в @Before и выполняем успешный логин")
    public void shouldLoginExistingUser() {
        userSteps.login(credsFrom(createdUser))
                .then()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Логин с неверным email возвращает 401")
    @Description("Передаём неверный email при корректном пароле")
    public void shouldNotLoginWithWrongEmail() {
        UserLoginRequest creds = credsFrom(createdUser);
        creds.setEmail("wrong_" + creds.getEmail());

        userSteps.login(creds)
                .then()
                .statusCode(HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным password возвращает 401")
    @Description("Передаём неверный пароль при корректном email")
    public void shouldNotLoginWithWrongPassword() {
        UserLoginRequest creds = credsFrom(createdUser);
        creds.setPassword("wrong_password");

        userSteps.login(creds)
                .then()
                .statusCode(HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
