package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.util.List;

@Controller
public class MealRestController {
    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public void delete(Integer userId, Integer id) {
        service.delete(userId, id);
    }

    public List<MealTo> getAll() {
        return service.getAll();
    }

    public void save(Integer userId, Meal meal) {
        service.save(userId, meal);
    }

    public Meal get(Integer userId, Integer id) {
        return service.get(userId, id);
    }

}