package nl.joey.peschier

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import retrofit2.Response

class MyAdapter(private val items: List<ArticleDTO>, private val positionListener: MyPositionListener) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(v as CardView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = items[position].Title
        holder.image.load(items[position].Image){
            crossfade(true)
            when(items[position].Feed){
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

        if(items[position].IsLiked){
            holder.liked.setImageResource(R.drawable.ic_heart_full)
        }
        else {
            holder.liked.setImageResource(R.drawable.ic_heart_empty)
        }

        holder.itemView.setOnClickListener{
            positionListener.onItemClicked(holder.adapterPosition)
        }

    }

    override fun getItemCount(): Int = items.size
}