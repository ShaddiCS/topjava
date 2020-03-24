package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController{

    @GetMapping
    public String list(Model model) {
        List<MealTo> mealTos = super.getAll();

        model.addAttribute("meals", mealTos);
        return "meals";
    }

    @GetMapping("filter")
    public String filter(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
                         Model model) {
        List<MealTo> mealList = super.getBetween(startDate, startTime, endDate, endTime);

        model.addAttribute("meals", mealList);
        return "meals";
    }

    @PostMapping
    public String save(@RequestParam(required = false) Integer id,
                       @RequestParam Integer calories,
                       @RequestParam String description,
                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                               LocalDateTime dateTime) {
        Meal meal = new Meal(dateTime, description, calories);

        if (id == null) {
            super.create(meal);
        } else {
            super.update(meal, id);
        }

        return "redirect:meals";
    }

    @GetMapping("{id}")
    public String getOne(@PathVariable("id") Integer id, Model model) {
        Meal meal = super.get(id);

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
        super.delete(id);

        return "redirect:/meals";
    }
}
