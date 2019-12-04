package com.brainykat.kariuki.ksd.Retrofits.Pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meal {
    @SerializedName("MealType")
    @Expose
    private Integer mealType;
    @SerializedName("POSId")
    @Expose
    private Long pOSId;

    public Integer getMealType() {
        return mealType;
    }

    public void setMealType(Integer mealType) {
        this.mealType = mealType;
    }

    public Long getPOSId() {
        return pOSId;
    }

    public void setPOSId(Long pOSId) {
        this.pOSId = pOSId;
    }
}
