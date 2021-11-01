package nl.joey.peschier

import com.google.gson.annotations.SerializedName

data class LoginResponse(@SerializedName("AuthToken") val AuthToken: String)
