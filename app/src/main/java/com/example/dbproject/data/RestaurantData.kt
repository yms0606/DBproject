package com.example.dbproject.data

data class RestaurantData(
    var id: Int? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var menu: ArrayList<String> = arrayListOf<String>(),
    var menuRating: ArrayList<Float> = arrayListOf<Float>(),
    var name: String? = null,
    var rating: Double? = 0.0,
    var reviewCount: Int? = 0,
)