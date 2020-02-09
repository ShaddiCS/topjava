package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealsRepo {
    List<Meal> getAll();

    Meal getById(int id);

    void add(Meal meal);

    void delete(int id);

    void update(Meal meal);
}
