package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
public class MealService {
    private MealRepository mealRepository;

    @Autowired
    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    public void delete(Integer userId, Integer id) {
        if (!mealRepository.delete(userId, id)) {
            throw new NotFoundException("Bad meal id");
        }
    }

    public List<MealTo> getAll() {
        return MealsUtil.getTos(mealRepository.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY)
                .stream().sorted(comparing(MealTo::getDateTime).reversed()).collect(toList());
    }

    public void save(Integer userId, Meal meal) {
        final Meal meal1 = mealRepository.save(userId, meal);
        if (meal1 == null) {
            throw new NotFoundException("Bad meal id");
        }
    }

    public Meal get(Integer userId, Integer id) {
        final Meal meal = mealRepository.get(userId, id);
        if (meal == null) {
            throw new NotFoundException("Bad meal id");
        }
        return meal;
    }
}