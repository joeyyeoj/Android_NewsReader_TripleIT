package nl.joey.peschier

import com.google.gson.annotations.SerializedName

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList


data class ArticleDTO(
    @SerializedName("Id")
    var Id: Int = 0,
    @SerializedName("Feed")
    var Feed: Int,
    @SerializedName("Title")
    var Title: String,
    @SerializedName("Summary")
    var Summary: String,
    @SerializedName("PublishDate")
    var PublishDate: String,
    @SerializedName("Image")
    var Image: String,
    @SerializedName("Url")
    var Url: String,
    @SerializedName("Related")
    var Related: List<String>,
    @SerializedName("Categories")
    var Categories: List<category>,
    @SerializedName("IsLiked")
    var IsLiked: Boolean
) : Serializable

data class category(
    @SerializedName("Id")
    var Id: Int,
    @SerializedName("Name")
    var Name: String) : Serializable
