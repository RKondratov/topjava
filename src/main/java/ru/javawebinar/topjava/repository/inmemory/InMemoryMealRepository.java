package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal.getUserId(), meal));
    }

    @Override
    public Meal save(Integer userId, Meal meal) {
        if(meal.getUserId() == null || !meal.getUserId().equals(userId)){
            return null;
        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(Integer userId, int id) {
        if (repository.get(id) == null || !repository.get(id).getUserId().equals(userId)) {
            return false;
        }
        repository.remove(id);
        return true;
    }

    @Override
    public Meal get(Integer userId, int id) {
        if (repository.get(id) == null || !repository.get(id).getUserId().equals(userId)) {
            return null;
        }
        return repository.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return repository.values().stream().sorted(comparing(Meal::getDate).reversed()).collect(toList());
    }
}

