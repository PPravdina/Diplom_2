package api;

import api.client.UserClient;
import api.util.TestDataGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static api.util.TestDataGenerator.faker;
import static org.hamcrest.CoreMatchers.equalTo;

public class UpdateUserTest extends BaseTest {
    @Override
    @Before
    public void setUp() {
        super.setUp();
        userClient = new UserClient(user);
        Response response = userClient.registerUser();
        user.setAccessToken(response.jsonPath().getString("accessToken"));
    }

    @Test
    @DisplayName("Update user Email")
    @Description("Проверка, что можно изменить имейл")
    public void updateUserEmailTest() {
        //Смена логина
        user.setEmail(faker.internet().emailAddress());
        Response response = userClient.updateUser(user);
        // Проверка, что смена логина прошла успешно
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("user.email", equalTo(user.getEmail()))
                .and()
                .body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Update user Name")
    @Description("Проверка, что можно изменить имя")
    public void updateUserNameTest() {
        //Смена имени
        user.setName(faker.name().firstName());
        Response response = userClient.updateUser(user);
        // Проверка, что смена логина прошла успешно
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("user.email", equalTo(user.getEmail()))
                .and()
                .body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Update email user without login")
    @Description("Проверка, что без авторизации нельзя изменить имейл")
    public void updateUserEmailWithoutLoginTest() {
        String copeAccessToken = user.getAccessToken();
        // Убрали авторизацию
        user.setAccessToken(null);
        //изменили данные
        user.setEmail(faker.internet().emailAddress());
        // Вызываем updateUser без передачи user с токеном
        Response response = userClient.updateUser(user);

        // Проверка, что смена логина прошла успешно
        response.then().assertThat().statusCode(401)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
        //Устанавливаем AccessToken обратно, чтобы удалить тестового пользователя
        user.setAccessToken(copeAccessToken);
    }

    @Test
    @DisplayName("Update name user without login")
    @Description("Проверка, что без авторизации нельзя изменить имя")
    public void updateUserNameWithoutLoginTest() {
        //Сохраняем токен в переменную, для дальнейшего удаления пользователя
        String copeAccessToken = user.getAccessToken();
        // Убрали авторизацию
        user.setAccessToken(null);
        //изменили данные
        user.setName(faker.name().firstName());
        // Вызываем updateUser без передачи user с токеном
        Response response = userClient.updateUser(user);

        // Проверка, что смена логина прошла успешно
        response.then().assertThat().statusCode(401)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
        //Устанавливаем AccessToken обратно, чтобы удалить тестового пользователя
        user.setAccessToken(copeAccessToken);
    }
}
