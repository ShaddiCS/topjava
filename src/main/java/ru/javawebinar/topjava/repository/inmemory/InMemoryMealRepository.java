package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> this.save(1, meal));
        MealsUtil.MEALS_SECOND.forEach(meal -> this.save(2, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        log.info("save {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.computeIfAbsent(userId, HashMap::new).put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.computeIfAbsent(userId, HashMap::new).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("delete {}", id);
        Map<Integer, Meal> mealMap = repository.get(userId);

        if (mealMap == null) {
            return false;
        }
        return mealMap.remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("get {}", id);
        Map<Integer, Meal> mealMap = repository.get(userId);

        if (mealMap == null) {
            return null;
        }
        return mealMap.get(id);
    }

    @Override
    public List<Meal> findByDateBetween(int userId, LocalDate fromDate, LocalDate toDate) {
        log.info("get all between {} and {}", fromDate, toDate);
        Map<Integer, Meal> mealMap = repository.get(userId);

        return getWithPredicate(mealMap.values(), meal -> DateTimeUtil.isBetweenInclusive(meal.getDate(), fromDate, toDate));
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("get all");
        Map<Integer, Meal> mealMap = repository.get(userId);

        return getWithPredicate(mealMap.values(), meal -> true);
    }

    private List<Meal> getWithPredicate(Collection<Meal> meals, Predicate<Meal> filter) {
        return meals.stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

