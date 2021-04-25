package com.team03.cocktailrecipesapp

data class Recipe(val cocktail_name: String, val cocktail_rating: Float, val cocktail_times_rated: Int) {
    var name: String = cocktail_name
    var preptime_minutes: Int = 5
    var difficulty: Int = 3
    var instruction: String = "step 1"
    var creation_time: String = ""
    var creator_username: String = "creator"
    var rating: Float = cocktail_rating
    var times_rated: Int = cocktail_times_rated
}
