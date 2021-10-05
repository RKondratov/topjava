package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(filteredByStreamsOptional(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        final Map<UserMeal, LocalDate> userMealDateMap = new HashMap<>();
        final Map<LocalDate, Integer> dateAndCaloriesMap = new HashMap<>();
        for (UserMeal userMeal : meals) {
            userMealDateMap.put(userMeal, userMeal.getDateTime().toLocalDate());
            dateAndCaloriesMap.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
        }
        final List<UserMealWithExcess> list = new ArrayList<>();

        for (UserMeal userMeal : userMealDateMap.keySet()) {
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                list.add(new UserMealWithExcess(userMeal.getDateTime(),
                        userMeal.getDescription(),
                        userMeal.getCalories(),
                        dateAndCaloriesMap.get(userMealDateMap.get(userMeal)) > caloriesPerDay));
            }
        }
        return list;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime,
                                                             LocalTime endTime, int caloriesPerDay) {
        final Map<UserMeal, LocalDate> userMealDateMap = new HashMap<>();
        final Map<LocalDate, Integer> dateAndCaloriesMap = new HashMap<>();
        meals.forEach(userMeal -> {
            userMealDateMap.put(userMeal, userMeal.getDateTime().toLocalDate());
            dateAndCaloriesMap.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
        });
        return userMealDateMap.keySet().stream()
                .filter(v -> TimeUtil.isBetweenHalfOpen(v.getDateTime().toLocalTime(), startTime, endTime))
                .map(v -> new UserMealWithExcess(v.getDateTime(),
                        v.getDescription(),
                        v.getCalories(),
                        dateAndCaloriesMap.get(userMealDateMap.get(v)) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByStreamsOptional(List<UserMeal> meals, LocalTime startTime,
                                                                     LocalTime endTime, int caloriesPerDay) {
        return meals.stream().collect(partitioningAndFiltering(startTime, endTime, caloriesPerDay));
    }

    public static Collector<UserMeal, Map.Entry<List<UserMeal>, Map<LocalDate, Integer>>, List<UserMealWithExcess>>
    partitioningAndFiltering(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return Collector.of(
                // supplier
                () -> new AbstractMap.SimpleImmutableEntry<>(
                        new ArrayList<>(), new HashMap<>()),

                // accumulator
                (c, e) -> {
                    c.getKey().add(e);
                    c.getValue().merge(e.getDateTime().toLocalDate(), e.getCalories(), Integer::sum);
                },

                // combiner
                (c1, c2) -> {
                    c1.getKey().addAll(c2.getKey());
                    return c1;
                },

                // finisher
                c -> c.getKey().stream()
                        .filter(v -> TimeUtil.isBetweenHalfOpen(v.getDateTime().toLocalTime(), startTime, endTime))
                        .map(v -> new UserMealWithExcess(v.getDateTime(), v.getDescription(), v.getCalories(),
                                c.getValue().get(v.getDateTime().toLocalDate()) > caloriesPerDay))
                        .collect(Collectors.toList()));
    }
}
