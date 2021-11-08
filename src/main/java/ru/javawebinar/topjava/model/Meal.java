package ru.javawebinar.topjava.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedQueries({
        @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal m WHERE m.id=:id AND m.user.id =:userId"),
        @NamedQuery(name = Meal.BY_USER_ID_AND_MEAL_ID, query = "SELECT m FROM Meal m WHERE m.id=:id AND m.user.id =:userId"),
        @NamedQuery(name = Meal.BY_USER_ID, query = "SELECT m FROM Meal m WHERE m.user.id =:userId ORDER BY m.dateTime desc"),
        @NamedQuery(name = Meal.BY_TIME, query = "SELECT m FROM Meal m WHERE m.user.id =:userId AND m.dateTime >=:startDateTime AND m.dateTime <:endDateTime ORDER BY m.dateTime DESC")
})
@Entity
@Table(name = "meals")
public class Meal extends AbstractBaseEntity {

    public static final String DELETE = "Meal.delete";
    public static final String BY_USER_ID_AND_MEAL_ID = "Meal.selectByUserIDAndMealId";
    public static final String BY_USER_ID = "Meal.selectByUserId";
    public static final String BY_TIME = "Meal.selectWithTime";

    @Column(name = "date_Time", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false)
    @NotBlank
    private String description;

    @Column(name = "calories", nullable = false)
    @NotNull
    private Integer calories;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
