package api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;

public class IngredientClient extends BaseClient {

    private static final String INGREDIENTS_PATH = "/api/ingredients";

    @Step("Get Ingredients")
    public static Response getIngredients() {
        return getRequestSpecification()
                .get(INGREDIENTS_PATH);

    }
}
