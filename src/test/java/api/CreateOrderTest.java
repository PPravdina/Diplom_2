package api;

import api.client.OrderClient;
import api.client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest extends BaseTest {
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
    @DisplayName("Create order with login")
    @Description("Проверка, что можно создать заказ авторизованным пользователем")
    public void createOrderWithLoginTest() {
        Response response = orderClient.createOrder();
        // Проверка, что заказ создан успешно
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Create order without login")
    @Description("Проверка, что можно создать заказ неавторизованным пользователем")
    public void createOrderWithoutLoginTest() {
        String copeAccessToken = user.getAccessToken();
        // Убрали авторизацию
        user.setAccessToken(null);
        Response response = orderClient.createOrder();
        // Проверка, что заказ создан успешно
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
        //Устанавливаем AccessToken обратно, чтобы удалить тестового пользователя
        user.setAccessToken(copeAccessToken);
    }

    @Test
    @DisplayName("Create order without ingredients")
    @Description("Проверка, что нельзя создать заказ без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        order.setIngredients(null);
        Response response = orderClient.createOrder();
        // Проверка, что заказ не создается
        response.then().assertThat().statusCode(400)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Create order without ingredients")
    @Description("Проверка, что нельзя создать заказ с несуществующим ингредиентом")
    public void createOrderWithWrongIngredientsTest() {
        order.setIngredients(Collections.singletonList("wrong"));
        Response response = orderClient.createOrder();
        // Проверка, что заказ не создается
        response.then().assertThat().statusCode(500);
    }
}
