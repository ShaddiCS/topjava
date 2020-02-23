package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Comparator;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID, USER_ID);
        assertMatch(meal, TEST_MEAL);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getWrongUser() throws Exception {
        service.get(MEAL_ID, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void delete() throws Exception{
        service.delete(MEAL_ID, USER_ID);
        service.get(MEAL_ID, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteWrongUser() throws Exception {
        service.delete(MEAL_ID, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        service.delete(1, USER_ID);
    }

    @Test
    public void getBetweenHalfOpen() {
        List<Meal> meals = service.getBetweenHalfOpen(START_DATE, END_DATE, ADMIN_ID);
        assertMatch(meals, ADMIN_DINNER, ADMIN_LUNCH);
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        List<Meal> sortedActual = MEALS;
        sortedActual.sort(Comparator.comparing(Meal::getDateTime).reversed());
        assertMatch(sortedActual, meals);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL_ID, USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateWrongUser() {
        Meal updated = getUpdated();
        service.update(updated, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal newMeal = MealTestData.getNew();
        Meal created = service.create(newMeal, UserTestData.USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, UserTestData.USER_ID), newMeal);
    }
}