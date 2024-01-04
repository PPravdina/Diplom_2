package api;

import api.client.OrderClient;
import api.client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrdersTest extends BaseTest {
    @Override
    @Before
    public void setUp() {
        super.setUp();
        userClient = new UserClient(user);
        Response response = userClient.registerUser();
        user.setAccessToken(response.jsonPath().getString("accessToken"));
        orderClient = new OrderClient(order, user);
    }

    @Test
    @DisplayName("Get orders with login")
    @Description("Проверка, что можно получить заказы авторизованного пользователя")
    public void getOrdersWithLoginTest() {
        //создаем заказ для пользователя
        orderClient.createOrder();
        Response response = orderClient.getUsersOrders();
        // Проверка, что список заказов получен
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Get orders without login")
    @Description("Проверка, что можно получить заказы авторизованного пользователя")
    public void getOrdersWithoutLoginTest() {
        //создаем заказ для пользователя
        orderClient.createOrder();
        String copeAccessToken = user.getAccessToken();
        // Убрали авторизацию
        user.setAccessToken(null);
        Response response = orderClient.getUsersOrders();
        // Проверка, что список заказов нельзя получить
        response.then().assertThat().statusCode(401)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
        //Устанавливаем AccessToken обратно, чтобы удалить тестового пользователя
        user.setAccessToken(copeAccessToken);
    }
}
