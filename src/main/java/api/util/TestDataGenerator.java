package api.util;

import api.model.User;
import com.github.javafaker.Faker;

public class TestDataGenerator {

    public static final Faker faker = new Faker();

    // Генерация рандомного пользователя с использованием JavaFaker
    public static User generateRandomUser() {
        User user = new User();
        user.setEmail(faker.internet().emailAddress());
        user.setPassword(faker.internet().password());
        user.setName(faker.name().firstName());

        return user;
    }
}
