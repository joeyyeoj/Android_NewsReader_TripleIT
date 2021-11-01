package nl.joey.peschier


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClientInstance {

    private var retrofit: Retrofit? = null
    private val BASE_URL = "https://inhollandbackend.azurewebsites.net/api/"



    val retrofitInstance: Retrofit?
        get(){
            if(retrofit == null){
                val httpClient = OkHttpClient.Builder().addInterceptor(Interceptor()).build()
                retrofit = retrofit2.Retrofit.Builder().client(httpClient).baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
            }
            return retrofit
        }
}