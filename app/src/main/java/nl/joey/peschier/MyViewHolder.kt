package nl.joey.peschier

import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class MyViewHolder(cardView: CardView) : RecyclerView.ViewHolder(cardView) {
    val title: TextView = cardView.findViewById(R.id.recyclerview_title)
    val image: ImageView = cardView.findViewById(R.id.recyclerview_image)
    val liked: ImageView = cardView.findViewById(R.id.recyclerview_liked)
}