package api.client;

import api.model.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserClient extends BaseClient {
    private static final String USER_PATH = "/api/auth/";
    private final User user;

    public UserClient(User user) {
        this.user = user;
    }

    @Step("Register User")
    public Response registerUser() {
        return getRequestSpecification()
                .and()
                .body(user)
                .when()
                .post(USER_PATH + "register");
    }

    @Step("Login User")
    public Response loginUser() {
        return getRequestSpecification()
                .and()
                .body(user)
                .when()
                .post(USER_PATH + "login");
    }

    @Step("Update User")
    public Response updateUser(User user) {
        RequestSpecification requestSpecification = getRequestSpecification();

        // Устанавливаем заголовок авторизации только если токен не равен null
        if (user.getAccessToken() != null) {
            requestSpecification.header("Authorization", user.getAccessToken());
        }

        return requestSpecification
                .body(user)
                .when()
                .patch(USER_PATH + "user");
    }


    @Step("Delete User")
    public Response deleteUser() {
        return getRequestSpecification()
                .header("Authorization", user.getAccessToken())
                .delete(USER_PATH + "user");
    }
}
