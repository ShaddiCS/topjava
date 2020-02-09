package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealsRepo;
import ru.javawebinar.topjava.repository.MealsRepoMemoryImpl;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final String INSERT_OR_EDIT = "meal.jsp";
    private static final String LIST_MEAL = "meals.jsp";
    private MealsRepo MemoryRepo;
    private static final Logger Log = getLogger(MealServlet.class);

    public MealServlet() {
        super();
        MemoryRepo = MealsRepoMemoryImpl.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int calories = Integer.parseInt(req.getParameter("calories"));
        String description = req.getParameter("description");
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
        String id = req.getParameter("mealId");
        if (id == null || id.isEmpty()) {
            MemoryRepo.add(new Meal(dateTime, description, calories));
        } else {
            Meal meal = new Meal(dateTime, description, calories);
            meal.setId(Integer.parseInt(id));
            MemoryRepo.update(meal);
        }

        req.setAttribute("meals", MealsUtil.filteredByStreams(MemoryRepo.getAll(), LocalTime.MIN, LocalTime.MAX, 2000));
        req.getRequestDispatcher("meals.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String forward;
        String action = req.getParameter("action");

        if (action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(req.getParameter("mealId"));
            MemoryRepo.delete(mealId);
            forward = LIST_MEAL;
            List<MealTo> mealToList = MealsUtil.filteredByStreams(MemoryRepo.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
            req.setAttribute("meals", mealToList);
        } else if (action.equalsIgnoreCase("edit")) {
            int mealId = Integer.parseInt(req.getParameter("mealId"));
            forward = INSERT_OR_EDIT;
            Meal meal = MemoryRepo.getById(mealId);
            req.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("listMeals")) {
            forward = LIST_MEAL;
            List<MealTo> mealToList = MealsUtil.filteredByStreams(MemoryRepo.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
            req.setAttribute("meals", mealToList);
        } else {
            forward = INSERT_OR_EDIT;
        }

        Log.debug("redirect to meal list");
        req.getRequestDispatcher(forward).forward(req, resp);
    }
}
