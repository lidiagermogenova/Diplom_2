package base;

import io.restassured.response.Response;
import model.user.UserCreate;
import model.user.UserLoginRequest;
import org.junit.After;
import org.junit.Before;
import steps.UserSteps;

import static java.net.HttpURLConnection.HTTP_OK;
import static testdata.UserGenerator.credsFrom;
import static testdata.UserGenerator.randomUser;

public class UserBaseTest extends BaseApiTest {

    protected final UserSteps userSteps = new UserSteps();

    protected UserCreate createdUser;
    protected String accessToken;

    @Before
    public void registerAndRememberTokens() {
        createdUser = randomUser();
        Response registerResponse = userSteps.register(createdUser);
        System.out.println("REGISTER STATUS: " + registerResponse.statusCode());
        System.out.println("REGISTER BODY: " + registerResponse.asString());
                //.then()
                //.statusCode(HTTP_OK);
        Response r = userSteps.login(credsFrom(createdUser));
        accessToken = r.then().extract().path("accessToken"); // "Bearer ..."
    }

    protected void loginAndRememberTokens() {
        UserLoginRequest creds = credsFrom(createdUser);
        Response r = userSteps.login(creds);
        accessToken = r.then().extract().path("accessToken");
    }

    @After
    public void cleanup() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userSteps.deleteUser(accessToken).then().extract().response();
        }
    }
}
