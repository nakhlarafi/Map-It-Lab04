package com.example.newdatafetching;

import android.location.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class Fetcher {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("location")
    @Expose
    private Object location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }
}
