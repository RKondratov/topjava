package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

@WebServlet("/meals")
public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    public static final int CALORIES_PER_DAY = 2000;
    public static final String DELETE = "delete";
    public static final String ACTION = "action";
    public static final String MEAL_ID = "mealId";
    public static final String UPDATE = "update";
    public static final String CREATE = "create";
    public static final String MEAL = "meal";
    private final List<Meal> meals = new ArrayList<>(Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 00), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 00), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 00), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 00), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 00), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 00), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 00), "Ужин", 410)
    ));

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter(ACTION);
        if (action == null) {
            extracted(request, response);
        } else if (action.equalsIgnoreCase(DELETE)) {
            Integer idForDelete = Integer.parseInt(request.getParameter(MEAL_ID));
            log.debug(String.format("Delete meal with id = %s", idForDelete));
            meals.removeIf(v -> v.getId().equals(idForDelete));
            extracted(request, response);
        } else if (action.equalsIgnoreCase(UPDATE)) {
            Integer idForUpdate = Integer.parseInt(request.getParameter(MEAL_ID));
            Meal meal = null;
            for (Meal v : meals) {
                if (v.getId().equals(idForUpdate)) {
                    meal = v;
                }
            }
            request.setAttribute(MEAL, meal);
            request.getRequestDispatcher("/create.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase(CREATE)) {
            request.getRequestDispatcher("/create.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!request.getParameter("id").isEmpty()) {
            Integer idForUpdate = Integer.parseInt(request.getParameter("id"));
            meals.removeIf(v -> v.getId().equals(idForUpdate));
            meals.add(new Meal(idForUpdate,
                    LocalDateTime.parse(request.getParameter("dateTime").replace("T", " "),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories"))));
        } else {
            meals.add(new Meal(LocalDateTime.parse(request.getParameter("dateTime").replace("T", " "),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories"))));
        }
        extracted(request, response);
    }

    private void extracted(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("mealsTo", filteredByStreams(meals, LocalTime.of(0, 0),
                LocalTime.of(23, 59, 59, 999), CALORIES_PER_DAY));
        log.debug("forward to meals");
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}

