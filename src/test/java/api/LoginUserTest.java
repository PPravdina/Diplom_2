package api;

import api.client.UserClient;
import api.util.TestDataGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest extends BaseTest {
    @Override
    @Before
    public void setUp() {
        super.setUp();
        userClient = new UserClient(user);
        userClient.registerUser();
    }

    @Test
    @DisplayName("Login user test")
    @Description("В результате теста должен быть авторизован пользователь")
    public void testLoginUserSuccessfully() {
        Response response = userClient.loginUser();
        // Проверка успешной авторизации
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
        // Сохранение токена
        user.setAccessToken(response.jsonPath().getString("accessToken"));
    }

    @Test
    @DisplayName("Wrong email login user test")
    @Description("В результате теста пользователь не авторизуется")
    public void testLoginUserWithWrongEmail() {
        Response response = userClient.loginUser();
        // Сохранение токена
        user.setAccessToken(response.jsonPath().getString("accessToken"));
        //Смена имейла
        user.setEmail(TestDataGenerator.generateRandomString(8) + "@example.com");
        Response responseLogin = userClient.loginUser();
        // Проверка, что авторизация не прошла
        responseLogin.then().assertThat().statusCode(401)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Wrong password login user test")
    @Description("В результате теста пользователь не авторизуется")
    public void testLoginUserWithWrongPassword() {
        Response response = userClient.loginUser();
        // Сохранение токена
        user.setAccessToken(response.jsonPath().getString("accessToken"));
        //Смена пароля
        user.setPassword(TestDataGenerator.generateRandomString(8));
        Response responseLogin = userClient.loginUser();
        // Проверка, что авторизация не прошла
        responseLogin.then().assertThat().statusCode(401)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
}
