package nl.joey.peschier

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class Interceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        if (request.url().encodedPath().equals("/api/Articles/") || request.url().encodedPath().startsWith("/api//Articles")) {
            return chain.proceed(request)
        }
        val newRequest: Request = request.newBuilder()
            .addHeader("x-authtoken", AppPreferences.token)
            .build()
        return chain.proceed(newRequest)
    }
}