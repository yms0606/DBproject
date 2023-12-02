package com.example.dbproject.data

data class RestaurantData(
    var name: String? = null,
    var id: Int? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var reviewCount: Int? = 0,
    var rating: Float? = 0.0f,
    var menu: ArrayList<String> = arrayListOf<String>(),
    var menuRating: ArrayList<Float> = arrayListOf<Float>()
)