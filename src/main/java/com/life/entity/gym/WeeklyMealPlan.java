package com.life.entity.gym;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="weekly_meal_plan")
public class WeeklyMealPlan {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String dayName;

    private String mealType;

    private String mealDescription;

    public WeeklyMealPlan() {
    }

	public WeeklyMealPlan(Long id, Long userId, String dayName, String mealType, String mealDescription) {
		super();
		this.id = id;
		this.userId = userId;
		this.dayName = dayName;
		this.mealType = mealType;
		this.mealDescription = mealDescription;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getDayName() {
		return dayName;
	}

	public void setDayName(String dayName) {
		this.dayName = dayName;
	}

	public String getMealType() {
		return mealType;
	}

	public void setMealType(String mealType) {
		this.mealType = mealType;
	}

	public String getMealDescription() {
		return mealDescription;
	}

	public void setMealDescription(String mealDescription) {
		this.mealDescription = mealDescription;
	}
    
}