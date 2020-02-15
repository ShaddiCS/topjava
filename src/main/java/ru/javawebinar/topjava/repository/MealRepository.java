package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.Collection;

public interface MealRepository {
    // null if not found, when updated
    Meal save(Meal meal);

    // false if not found
    boolean delete(int userId, int id);

    // null if not found
    Meal get(int userId, int id);

    Collection<Meal> findByDateBetween(int userId, int calories, LocalDate fromDate, LocalDate toDate);
}
