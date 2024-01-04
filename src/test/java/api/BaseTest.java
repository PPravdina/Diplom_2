package api;

import api.client.OrderClient;
import api.client.UserClient;
import api.model.Order;
import api.model.User;
import api.util.OrderGenerator;
import api.util.TestDataGenerator;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;

import static org.hamcrest.CoreMatchers.equalTo;

public class BaseTest {
    protected User user;
    protected UserClient userClient;
    protected Order order;
    protected OrderClient orderClient;

    @Before
    public void setUp() {
        TestDataGenerator testDataGenerator = new TestDataGenerator();
        initializeTestData(testDataGenerator);
        initializeTestOrderData(testDataGenerator);
    }

    private void initializeTestData(TestDataGenerator testDataGenerator) {
        user = TestDataGenerator.generateRandomUser();
    }

    private void initializeTestOrderData(TestDataGenerator testDataGenerator) {
        order = OrderGenerator.generateOrder();
    }

    @After
    public void deleteUser() {
        if (user != null && user.getAccessToken() != null) {
            Response response = userClient.deleteUser();
            response
                    .then()
                    .log().all()
                    .statusCode(202)
                    .body("success", equalTo(true))
                    .and()
                    .body("message", equalTo("User successfully removed"));
        }
    }

}
