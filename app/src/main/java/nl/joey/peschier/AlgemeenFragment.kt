package nl.joey.peschier

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AlgemeenFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener{

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var createdAdapter: MyAdapter
    private lateinit var recyclerView: RecyclerView
    private val service = RetrofitClientInstance.retrofitInstance?.create(GetArticleService::class.java)
    private val articles = mutableListOf<ArticleDTO>()
    private var feedId: Int = 0
    private var nextId: Int = 0
    private var fetching: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.feed_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AppPreferences.init(requireContext())
        feedId = arguments?.getInt("FEED_ID")!!
        swipeRefreshLayout = view.findViewById(R.id.swipe_container)
        swipeRefreshLayout.setOnRefreshListener(this)
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        createdAdapter = MyAdapter(articles, object: MyPositionListener{
            override fun onItemClicked(position: Int) {
                val intent = Intent(context, DetailActivity::class.java)
                val sendThisArticle = articles[position]
                intent.putExtra("ARTICLE", sendThisArticle)
                startActivity(intent, savedInstanceState)
            }
        })
        if(feedId != 7){
            getArticles()
        }
        else {
            onlyGetFavorites()
        }
        recyclerView.apply{
            layoutManager = LinearLayoutManager(activity)
            adapter = createdAdapter
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(!recyclerView.canScrollVertically(1)){
                    getNextArticle()
                }
            }
        })
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onRefresh() {
        if(feedId != 7){
            getArticles()
        }
        else {
            onlyGetFavorites()
        }
    }

    fun getArticles(){
        articles.clear()
        val loggedIn = AppPreferences.isLogin
        var articleCall = service?.getArticlesByFeed(feedId)
        if (loggedIn) {
            if (service != null) {
                articleCall = service.loggedInArticlesByFeed(feedId)
            }
        }

        swipeRefreshLayout.setRefreshing(true)
        articleCall?.enqueue(object : Callback<ArticleList>{
            override fun onResponse(call: Call<ArticleList>, response: Response<ArticleList>) {
                val body = response.body()
                if (body != null) {
                    for(article in body.articles){
                        //hier add ik het artikel aan de
                        articles.add(article)
                    }
                    nextId = body.nextId
                    createdAdapter.notifyDataSetChanged()
                    swipeRefreshLayout.setRefreshing(false)
                }
            }
            override fun onFailure(call: Call<ArticleList>, t: Throwable) {
                Toast.makeText(context, "Er is iets misgegaan met het ophalen van het nieuws :(", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getNextArticle(){
        if(!fetching){
            val loggedIn = AppPreferences.isLogin
            var articleCall = service?.nextArticleByFeed(nextId, feedId)
            if (loggedIn) {
                if (service != null) {
                    articleCall = service.loggedInNextArticleByFeed(nextId, feedId)
                }
            }
            fetching = true
            articleCall?.enqueue(object : Callback<ArticleList>{
                override fun onResponse(call: Call<ArticleList>, response: Response<ArticleList>) {
                    val body = response.body()
                    if (body != null) {
                        nextId = body.nextId
                        for(article in body.articles){
                            articles.add(article)
                        }
                        createdAdapter.notifyDataSetChanged()
                        fetching = false
                    }
                    else{
                    }
                }
                override fun onFailure(call: Call<ArticleList>, t: Throwable) {
                    Toast.makeText(context, "Er is iets misgegaan met het ophalen van het nieuws :(", Toast.LENGTH_LONG).show()
                }
            })
        }

    }

    fun onlyGetFavorites(){
        //hier hardcode ik feedId 0 zodat hij altijd alle feeds ophaalt, daardoor kan ik in de bundle die ik meegeef vanuit de main een ander getal meegeven zodat ik hier kan detecteren wanneer ik op de favorieten pagina zit
        articles.clear()
        val articleCall = service?.likedArticles(0)
        swipeRefreshLayout.setRefreshing(true)
        articleCall?.enqueue(object : Callback<ArticleList>{
            override fun onResponse(call: Call<ArticleList>, response: Response<ArticleList>) {
                val body = response.body()
                if (body != null) {
                    nextId = body.nextId
                    for(article in body.articles){
                        articles.add(article)
                    }
                    createdAdapter.notifyDataSetChanged()
                    swipeRefreshLayout.setRefreshing(false)
                }
                else{
                }
            }
            override fun onFailure(call: Call<ArticleList>, t: Throwable) {
                Toast.makeText(context, "Er is iets misgegaan met het ophalen van je favoriete nieuws :(", Toast.LENGTH_LONG).show()
                swipeRefreshLayout.setRefreshing(false)
            }
        })
    }




}