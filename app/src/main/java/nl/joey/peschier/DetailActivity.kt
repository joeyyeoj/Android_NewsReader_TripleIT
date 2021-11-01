package nl.joey.peschier

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import coil.load
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailActivity : AppCompatActivity() {

    companion object {
        const val ARTICLE = "ARTICLE"
    }

    private val service = RetrofitClientInstance.retrofitInstance?.create(GetArticleService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        AppPreferences.init(this)
        window.statusBarColor = getColor(R.color.design_default_color_primary_dark)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        if(toolbar != null){
            setSupportActionBar(toolbar)

        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val article = intent.getSerializableExtra(ARTICLE) as ArticleDTO
        val title = findViewById<TextView>(R.id.article_title)
        val text = findViewById<TextView>(R.id.article_text)
        val image = findViewById<ImageView>(R.id.article_image)
        val likebutton = findViewById<ImageView>(R.id.likebutton)
        val readmorebutton = findViewById<Button>(R.id.readmorebutton)
        val dateholder = findViewById<TextView>(R.id.article_date)
        val related = findViewById<LinearLayout>(R.id.detail_related)
        val categories = findViewById<LinearLayout>(R.id.detail_categories)

        image.load(article.Image){
            crossfade(true)
            when(article.Feed){
                0 -> {
                    placeholder(R.drawable.ic_laatste)
                }
                1 -> {
                    placeholder(R.drawable.ic_algemeen)
                }
                2 -> {
                    placeholder(R.drawable.ic_internet)
                }
                3 -> {
                    placeholder(R.drawable.ic_sport)
                }
                4 -> {
                    placeholder(R.drawable.ic_opmerkelijk)
                }
                5 -> {
                    placeholder(R.drawable.ic_games)
                }
                6 -> {
                    placeholder(R.drawable.ic_wetenschap)
                }
            }
        }

        val date = article.PublishDate.substring(0, 10)
        val time = article.PublishDate.substring(11, 16)

        dateholder.setText("Op $date om $time")
        title.setText(article.Title)
        text.setText(article.Summary)



        if(article.Related.size > 0){
            for(i in article.Related){
                val textview = TextView(this)
                textview.setText(i)
                textview.setTextColor(getColor(R.color.design_default_color_primary_dark))
                textview.paintFlags = textview.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                
                textview.setOnClickListener {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(i))
                    startActivity(browserIntent)
                }
                related.addView(textview)
            }
        }
        else{
            val textview = TextView(this)
            textview.setText(getString(R.string.norelatedarticles))
            related.addView(textview)
        }

        if(article.Categories.size > 0){
            for(i in article.Categories){
                val textview = TextView(this)
                textview.setText(i.Name)
                textview.setTextColor(getColor(R.color.white))
                textview.background = getDrawable(R.drawable.categorybubble)
                textview.paintFlags = textview.paintFlags or Paint.FAKE_BOLD_TEXT_FLAG
                categories.addView(textview)
            }
        }

        if(article.IsLiked){
            likebutton.setImageResource(R.drawable.ic_heart_full)
        }
        else{
            likebutton.setImageResource(R.drawable.ic_heart_empty)
        }

        likebutton.setOnClickListener {
            if(AppPreferences.isLogin){
                var likecall = service?.likeArticle(article.Id)
                if(article.IsLiked){
                    likecall = service?.unlikeArticle(article.Id)
                }

                if (likecall != null) {
                    likecall.enqueue(object: Callback<Void>{
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Log.d("Like", response.toString())
                            if(article.IsLiked){
                                likebutton.setImageResource(R.drawable.ic_heart_empty)
                            }
                            else{
                                likebutton.setImageResource(R.drawable.ic_heart_full)
                            }
                        }
                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(applicationContext, "Er is iets misgegaan met het liken/unliken van het artikel :(", Toast.LENGTH_LONG).show()
                        }

                    })
                }
            }
            else {
                Toast.makeText(applicationContext, "Je moet ingelogd zijn om een artikel te kunnen liken!", Toast.LENGTH_LONG).show()
            }
        }

        readmorebutton.setOnClickListener {
            val uri: Uri = Uri.parse(article.Url) // missing 'http://' will cause crashed
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}