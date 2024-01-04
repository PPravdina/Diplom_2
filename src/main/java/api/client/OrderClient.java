package api.client;

import api.model.Order;
import api.model.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class OrderClient extends BaseClient {
    private static final String ORDER_PATH = "/api/orders/";
    private final Order order;

    public OrderClient(Order order, User user) {
        this.order = order;
        this.user = user;
    }

    @Step("Create order")
    public Response createOrder() {
        RequestSpecification requestSpecification = getRequestSpecification();

        // Устанавливаем заголовок авторизации только если токен не равен null
        if (user.getAccessToken() != null) {
            requestSpecification.header("Authorization", user.getAccessToken());
        }

        return requestSpecification
                .body(order)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Get user's orders")
    public Response getUsersOrders() {
        RequestSpecification requestSpecification = getRequestSpecification();

        // Устанавливаем заголовок авторизации только если токен не равен null
        if (user.getAccessToken() != null) {
            requestSpecification.header("Authorization", user.getAccessToken());
        }

        return requestSpecification
                .when()
                .get(ORDER_PATH);
    }
}
