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
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        log.info("save {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.computeIfAbsent(meal.getUserId(), HashMap::new).put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.get(meal.getUserId()).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("delete {}", id);
        return repository.get(userId).remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("get {}", id);
        return repository.get(userId).get(id);
    }

    @Override
    public Collection<Meal> findByDateBetween(int userId, int calories, LocalDate fromDate, LocalDate toDate) {
        if(repository.get(userId) == null) {
            return new ArrayList<>();
        }
        return repository.get(userId).values().stream()
                .filter(meal -> DateTimeUtil.isBetweenInclusive(meal.getDate(), fromDate, toDate))
                .sorted(Comparator.comparing(Meal::getDate).reversed()
                    .thenComparing(Comparator.comparing(Meal::getTime).reversed()))
                .collect(Collectors.toList());
    }
}

