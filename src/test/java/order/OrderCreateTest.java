package order;

import base.UserBaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.order.OrderCreate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class OrderCreateTest extends UserBaseTest {
    private final OrderSteps orderSteps = new OrderSteps();

    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
    private List<String> getTwoValidIngredientsIds() {
        return orderSteps.getIngredients()
                .then()
                .statusCode(HTTP_OK)
                .extract()
                .path("data._id[0,1]");
    }

    @Test
    @DisplayName("Создание заказа с авторизацией успешно")
    @Description("Создаём пользователя в @Before, получаем accessToken и создаём заказ с валидными ингредиентами")
    public void shouldCreateOrderWithAuthorization() {
        OrderCreate order = new OrderCreate(getTwoValidIngredientsIds());

        orderSteps.createOrderAuth(accessToken, order)
                .then()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации успешно")
    @Description("Создаём заказ без токена с валидными ингредиентами и ожидаем success=true")
    public void shouldCreateOrderWithoutAuthorization() {
        OrderCreate order = new OrderCreate(getTwoValidIngredientsIds());

        orderSteps.createOrderNoAuth(order)
                .then()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true));
    }
    @Test
    @DisplayName("Нельзя создать заказ без ингредиентов")
    @Description("Отправляем пустой список ингредиентов авторизованным пользователем и ожидаем 400 и сообщение об ошибке")
    public void shouldNotCreateOrderWithoutIngredients() {
        OrderCreate order = new OrderCreate(Collections.<String>emptyList());

        orderSteps.createOrderAuth(accessToken, order)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиента возвращает 500")
    @Description("Отправляем некорректный id ингредиента авторизованным пользователем и ожидаем 500")
    public void shouldFailWithInvalidIngredientHash() {
        OrderCreate order = new OrderCreate(
                Arrays.asList("60d3463f7034a000269f45eZ")
        );

        orderSteps.createOrderAuth(accessToken, order)
                .then()
                .statusCode(HTTP_INTERNAL_ERROR);
    }
}
