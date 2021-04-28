package com.team03.cocktailrecipesapp;

import com.android.volley.Response;

import org.json.JSONObject;

public class TestListener implements Response.Listener<JSONObject> {
    boolean gotResponse = false;
    @Override
    public void onResponse(JSONObject response) {
        gotResponse = true;
    }
}
