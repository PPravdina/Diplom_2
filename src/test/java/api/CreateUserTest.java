package api;

import api.client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest extends BaseTest {
    @Override
    @Before
    public void setUp() {
        super.setUp();
        userClient = new UserClient(user);
    }

    @Test
    @DisplayName("Create user test")
    @Description("В результате теста должен быть зарегистрирован пользователь")
    public void testCreateUserSuccessfully() {
        Response response = userClient.registerUser();
        // Проверки успешной регистрации
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
        // Сохранение токенов
        user.setAccessToken(response.jsonPath().getString("accessToken"));
    }

    @Test
    @DisplayName("Create duplicate user test")
    @Description("Проверка, что нельзя создать два юзера с одинаковыми данными")
    public void testCreateDuplicateUser() {
        Response response1 = userClient.registerUser();
        Response response2 = userClient.registerUser();
        // Проверка, что регистрация не прошла
        response2.then().assertThat().statusCode(403)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));
        // Сохранение токенов
        user.setAccessToken(response1.jsonPath().getString("accessToken"));
    }

    @Test
    @DisplayName("Create user test without name")
    @Description("Проверка, что нельзя создать юзера без имени")
    public void testCreateUserWithoutName() {
        user.setName(null);// убираем имя
        Response response = userClient.registerUser();
        // Проверка, что регистрация не прошла
        response.then().assertThat().statusCode(403)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Create user test without email")
    @Description("Проверка, что нельзя создать юзера без имейла")
    public void testCreateUserWithoutEmail() {
        user.setEmail(null);// убираем имейл
        Response response = userClient.registerUser();
        // Проверка, что регистрация не прошла
        response.then().assertThat().statusCode(403)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Create user test without password")
    @Description("Проверка, что нельзя создать юзера без пароля")
    public void testCreateUserWithoutPassword() {
        user.setPassword(null);// убираем пароль
        Response response = userClient.registerUser();
        // Проверка, что регистрация не прошла
        response.then().assertThat().statusCode(403)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
