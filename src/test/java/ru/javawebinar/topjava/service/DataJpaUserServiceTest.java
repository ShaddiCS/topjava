package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles("datajpa")
public class DataJpaUserServiceTest extends UserServiceTest {

    @Test
    public void findWithUser() throws Exception {
        User actual = service.getWithMeal(ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual.getMeals(), ADMIN_MEAL2, ADMIN_MEAL1);
        USER_MATCHER.assertMatch(actual, ADMIN);
    }
}
