package com.team03.cocktailrecipesapp;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class TestErrorListener implements Response.ErrorListener {
    boolean gotResponse = false;
    @Override
    public void onErrorResponse(VolleyError error) {
        gotResponse = true;
    }
}
