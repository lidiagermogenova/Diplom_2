package testdata;

import com.github.javafaker.Faker;
import model.user.UserCreate;
import model.user.UserLoginRequest;

public class UserGenerator {
    private static final Faker faker = new Faker();

    public static UserCreate randomUser() {
        return new UserCreate(
                faker.internet().emailAddress(),
                faker.internet().password(6, 10, true, true),
                faker.name().firstName()
        );
    }

    public static UserLoginRequest credsFrom(UserCreate user) {
        return new UserLoginRequest(user.getEmail(), user.getPassword());
    }

    public static UserCreate withoutEmail() {
        UserCreate user = randomUser();
        user.setEmail(null);
        return user;
    }

    public static UserCreate withoutPassword() {
        UserCreate user = randomUser();
        user.setPassword(null);
        return user;
    }

    public static UserCreate withoutName() {
        UserCreate user = randomUser();
        user.setName(null);
        return user;
    }
}
