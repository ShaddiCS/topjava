package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealsRepo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger Log = getLogger(MealServlet.class);
    public final static MealsRepo mealsRepo = MealsRepo.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Log.debug("redirect to meal list");

        List<MealTo> mealToList = MealsUtil.filteredByStreams(mealsRepo.getAllMeals(), LocalTime.MIN, LocalTime.MAX, 2000);

        req.setAttribute("meals", mealToList);
        req.getRequestDispatcher("meals.jsp").forward(req, resp);
    }
}
