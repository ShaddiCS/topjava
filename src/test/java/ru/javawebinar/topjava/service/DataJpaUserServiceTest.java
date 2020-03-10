package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Test
    public void findWithMeal() throws Exception {
        User actual = service.getWithMeal(ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual.getMeals(), ADMIN_MEAL2, ADMIN_MEAL1);
        USER_MATCHER.assertMatch(actual, ADMIN);
    }

    @Test
    public void findWithNoMeal() throws Exception {
        User actual = service.getWithMeal(USER_NO_MEAL_ID);
        Assert.assertTrue(actual.getMeals() == null || actual.getMeals().isEmpty());
        USER_MATCHER.assertMatch(actual, USER_NO_MEALS);
    }

    @Test(expected = NotFoundException.class)
    public void withMealNotFound() throws Exception {
        service.getWithMeal(1);
    }
}
