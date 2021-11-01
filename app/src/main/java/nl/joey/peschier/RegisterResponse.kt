package nl.joey.peschier

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("Success")
    val succes: Boolean,
    @SerializedName("Message")
    val message: String
)
