package com.example.dbproject.data

data class ReviewData(
    var resName : String? = null,
    var resId : Int? = null,
    var userName: String? = null,
    var resRating: Double? = 0.0,
    var favorite: Int? = 0,
    var comment: String? = null,
    var imageUrl: String? = null,
    var favoriteList: ArrayList<String> = arrayListOf<String>(),
    var menuRating: ArrayList<Double> = arrayListOf<Double>(),
    var uid: String? = null,
    var userUid: String? = null
)