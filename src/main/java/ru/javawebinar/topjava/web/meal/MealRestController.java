package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(authUserId(), meal);
    }

    public void delete(int id) {
        log.info("Delete {}", id);
        service.delete(authUserId(), id);
    }

    public Meal get(int id) {
        log.info("git {}", id);
        return service.get(authUserId(), id);
    }

    public List<MealTo> getAll() {
        log.info("get all");
        return service.getAll(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(authUserId(), meal);
    }

    public List<MealTo> getFiltered(String fromDate, String toDate, String fromTime, String toTime) {
        log.info("get filtered");
        return service.getFiltered(authUserId(),
                authUserCaloriesPerDay(),
                fromDate == null  || fromDate.isEmpty() ? LocalDate.MIN : LocalDate.parse(fromDate),
                toDate == null || toDate.isEmpty() ? LocalDate.MAX : LocalDate.parse(toDate),
                fromTime == null || fromTime.isEmpty() ? LocalTime.MIN : LocalTime.parse(fromTime),
                toTime == null || toTime.isEmpty() ? LocalTime.MAX : LocalTime.parse(toTime));
    }
}