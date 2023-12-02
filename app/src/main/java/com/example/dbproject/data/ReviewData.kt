package com.example.dbproject.data

data class ReviewData(
    var resName : String? = null,
    var resId : Int? = null,
    var userName: String? = null,
    var resRating: Float? = 0.0f,
    var favorite: Int? = 0,
    var comment: String? = null,
    var imageUrl: String? = null,
    var favoriteList: ArrayList<String> = arrayListOf<String>(),
    var menuRating: ArrayList<Float> = arrayListOf<Float>()
)