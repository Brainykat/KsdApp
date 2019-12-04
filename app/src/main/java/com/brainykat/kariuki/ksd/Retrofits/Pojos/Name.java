package com.brainykat.kariuki.ksd.Retrofits.Pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Name {
    @SerializedName("sur")
    @Expose
    private String sur;
    @SerializedName("first")
    @Expose
    private String first;
    @SerializedName("middle")
    @Expose
    private String middle;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("fullNameReverse")
    @Expose
    private String fullNameReverse;

    public String getSur() {
        return sur;
    }

    public void setSur(String sur) {
        this.sur = sur;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullNameReverse() {
        return fullNameReverse;
    }

    public void setFullNameReverse(String fullNameReverse) {
        this.fullNameReverse = fullNameReverse;
    }
}
