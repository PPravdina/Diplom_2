package api.client;

import api.model.User;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseClient {

    protected static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    protected User user;

    protected static RequestSpecification getRequestSpecification() {
        return given()
                .baseUri(BASE_URI)
                .header("Content-Type", "application/json");
    }
}