package api.util;

import api.client.IngredientClient;
import api.model.Order;

public class OrderGenerator {
    public static Order generateOrder() {

        return new Order()
                .setIngredients(IngredientClient.getIngredients().path("data._id"));
    }
}
