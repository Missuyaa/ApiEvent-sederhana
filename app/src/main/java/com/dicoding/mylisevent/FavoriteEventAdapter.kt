package com.dicoding.mylisevent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mylisevent.model.FavoriteEvent

class FavoriteEventAdapter(
    private val onItemClick: (FavoriteEvent) -> Unit
) : ListAdapter<FavoriteEvent, FavoriteEventAdapter.FavoriteViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            onItemClick(event)
        }
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameEvent: TextView = itemView.findViewById(R.id.name_event)
        private val imageLogo: ImageView = itemView.findViewById(R.id.image_logo)

        fun bind(event: FavoriteEvent) {
            nameEvent.text = event.name

            val logoUrl = event.imageLogo
            if (logoUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(logoUrl)
                    .into(imageLogo)
            } else {
                imageLogo.setImageResource(R.drawable.ic_favorite_border) // Placeholder jika URL null
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteEvent>() {
            override fun areItemsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
                return oldItem == newItem
            }
        }
    }
}
