package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
        System.out.println();
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println();
        System.out.println("With java12: " + filteredByStreamsWithJava12(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> map = new HashMap<>();
        for (UserMeal meal : meals) {
            map.put(meal.getDateTime().toLocalDate(), map.getOrDefault(meal.getDateTime().toLocalDate(), 0) + meal.getCalories());
        }
        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenInclusive(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), map.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }

        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        return meals.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate()))
                .values().stream()
                .flatMap(dayMeals -> {
                    int calories = dayMeals.stream().mapToInt(UserMeal::getCalories).sum();
                    boolean exceeded = calories > caloriesPerDay;
                    return dayMeals.stream()
                            .filter(meal -> TimeUtil.isBetweenInclusive(meal.getDateTime().toLocalTime(), startTime, endTime))
                            .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceeded));
                }).collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByStreamsWithJava12(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        return meals.stream()
                .collect(Collectors.teeing(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)),
                        Collectors.filtering(meal -> TimeUtil.isBetweenInclusive(meal.getDateTime().toLocalTime(), startTime, endTime), Collectors.toList()),
                        (dateToCalories, filteredMeals) -> filteredMeals.stream()
                                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), dateToCalories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                                .collect(Collectors.toList())));
    }
}
