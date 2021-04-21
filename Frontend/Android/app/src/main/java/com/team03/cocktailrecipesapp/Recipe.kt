package com.team03.cocktailrecipesapp

data class Recipe(val cocktail_name: String) {
    var name: String = cocktail_name
    var preptime_minutes: Int = 5
    var difficulty: Int = 3
    var instruction: String = "step 1"
    var creation_time: String = ""
    var creator_username: String = "creator"
}
