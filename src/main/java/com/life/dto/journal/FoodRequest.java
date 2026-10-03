package com.life.dto.journal;

import com.life.entity.journal.FoodStatus;
import com.life.entity.journal.FoodType;

public class FoodRequest {
    private String name;
    private FoodType type;
    private FoodStatus status;
    private String description;

    public FoodRequest() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FoodType getType() {
        return type;
    }

    public void setType(FoodType type) {
        this.type = type;
    }

    public FoodStatus getStatus() {
        return status;
    }

    public void setStatus(FoodStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
