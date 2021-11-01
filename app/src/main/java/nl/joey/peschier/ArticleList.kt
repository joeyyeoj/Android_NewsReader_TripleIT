package nl.joey.peschier

import com.google.gson.annotations.SerializedName


data class ArticleList(
    @SerializedName("Results")
    var articles: List<ArticleDTO>,
    @SerializedName("NextId")
    var nextId: Int
    )