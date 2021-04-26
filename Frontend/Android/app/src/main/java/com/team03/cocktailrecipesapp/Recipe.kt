package com.team03.cocktailrecipesapp

import java.time.LocalDateTime

data class Recipe(
    var name: String? = null,
    var preptime_minutes: Int,
    var difficulty: Int,
    var instruction: String? = null,
    var creation_time: LocalDateTime? = null,
    var creator_username: String? = null
)