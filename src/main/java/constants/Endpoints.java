package constants;

public class Endpoints {

    // пользователь
    public static final String USER_REGISTER = "/api/auth/register"; //регистрация
    public static final String USER_LOGIN = "/api/auth/login"; //авторизация
    public static final String USER_LOGOUT = "/api/auth/logout"; //выход из системы
    public static final String USER_DELETE = "/api/auth/user"; //удалить пользователя

    // заказ
    public static final String CREATE_ORDER = "/api/orders"; //создание заказа
    public static final String INGREDIENTS = "/api/ingredients";
}
