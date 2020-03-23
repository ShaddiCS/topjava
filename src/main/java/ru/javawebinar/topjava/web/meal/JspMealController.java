package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/meals")
public class JspMealController {

    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private MealService mealService;

    @GetMapping
    public String list(Model model) {
        int userId = SecurityUtil.authUserId();

        log.info("getAll for user {}", userId);
        List<MealTo> mealTos = MealsUtil.getTos(mealService.getAll(userId), SecurityUtil.authUserCaloriesPerDay());

        model.addAttribute("meals", mealTos);
        return "meals";
    }

    @GetMapping("filter")
    public String filter(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
                         Model model) {
        int userId = SecurityUtil.authUserId();

        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);
        List<Meal> mealList = mealService.getBetweenInclusive(startDate, endDate, userId);

        model.addAttribute("meals", MealsUtil.getFilteredTos(mealList, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        return "meals";
    }

    @PostMapping
    public String save(@RequestParam(required = false) Integer id,
                       @RequestParam Integer calories,
                       @RequestParam String description,
                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                               LocalDateTime dateTime) {
        Meal meal = new Meal(dateTime, description, calories);
        int userId = SecurityUtil.authUserId();

        if (id == null) {
            log.info("create {} for user {}", meal, userId);
            mealService.create(meal, userId);
        } else {
            meal.setId(id);
            log.info("update {} for user {}", meal, userId);
            mealService.update(meal, userId);
        }

        return "redirect:meals";
    }

    @GetMapping("{id}")
    public String getOne(@PathVariable("id") Integer id, Model model) {
        int userId = SecurityUtil.authUserId();

        log.info("get meal {} for user {}", id, userId);
        Meal meal = mealService.get(id, userId);

        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/create")
    public String create(Model model) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        int userId = SecurityUtil.authUserId();

        log.info("delete meal {} for user {}", id, userId);
        mealService.delete(id, userId);

        return "redirect:/meals";
    }
}
