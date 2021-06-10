package com.team03.cocktailrecipesapp

class ImageUrl {
    lateinit var url: String
    constructor(_url: String?) {
        url = if (_url == null || _url.isEmpty() || _url.isBlank()) "something" else _url;
    }
}