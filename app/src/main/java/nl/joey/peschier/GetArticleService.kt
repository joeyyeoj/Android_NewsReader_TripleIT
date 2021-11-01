package nl.joey.peschier

import retrofit2.Call
import retrofit2.http.*

interface GetArticleService {

    companion object {
        var token = AppPreferences.token
    }

    @GET("Articles")
    fun getAllArticles(): Call<ArticleList>

    // heb er een / achter gezet zodat de Interceptor die uiteindelijk de header met het authtoken toevoegd ze uit elkaar kan halen, beetje hacky maar het werkt
    @GET("Articles/")
    fun getArticlesByFeed(@Query("feed") feedId: Int): Call<ArticleList>

    @GET("Articles")
    fun loggedInArticlesByFeed(@Query("feed") feedId: Int): Call<ArticleList>

    //Nogmaals een raar get URL stukje om te zorgen dat de interceptor het verschil kan zien.
    @GET("/api//Articles/{id}/")
    fun nextArticleByFeed(@Path("id") nextArticleId: Int, @Query("feed") feedId: Int, @Query("count") count: Int = 20): Call<ArticleList>

    @GET("Articles/{id}")
    fun loggedInNextArticleByFeed(@Path("id") nextArticleId: Int, @Query("feed") feedId: Int, @Query("count") count: Int = 20): Call<ArticleList>

    @GET("Articles/liked")
    fun likedArticles(@Query("feed") feedId: Int): Call<ArticleList>

    @PUT("Articles/{id}/like")
    fun likeArticle(@Path("id") articleId: Int): Call<Void>

    @DELETE("Articles/{id}/like")
    fun unlikeArticle(@Path("id") articleId: Int): Call<Void>

    @POST("Users/Login")
    fun login(@Body userInfo: User): Call<LoginResponse>

    @POST("Users/Register")
    fun register(@Body userInfo: User): Call<RegisterResponse>
}

