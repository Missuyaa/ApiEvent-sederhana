package com.dicoding.mylisevent

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mylisevent.model.Event

class EventAdapter(
    private var events: List<Event>,
    private val onItemClick: (Event) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventName: TextView = itemView.findViewById(R.id.name_event)
        private val eventImage: ImageView = itemView.findViewById(R.id.image_logo)

        fun bind(event: Event, onItemClick: (Event) -> Unit) {
            eventName.text = event.name
            Glide.with(itemView.context).load(event.imageLogo).into(eventImage)
            itemView.setOnClickListener { onItemClick(event) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        Log.d("EventAdapter", "Binding event: ${event.name}") // Tambahkan log ini untuk memastikan event di-bind dengan benar
        holder.bind(event, onItemClick)
    }


    override fun getItemCount(): Int = events.size

    fun updateData(newEvents: List<Event>) {
        Log.d("EventAdapter", "Updating data with ${newEvents.size} events")
        Log.d("EventAdapter", "First event: ${newEvents.firstOrNull()?.name ?: "No events"}")
        val oldItemCount = events.size
        events = newEvents
        if (oldItemCount == 0) {
            // Jika sebelumnya RecyclerView kosong, kita bisa menggunakan notifyItemRangeInserted
            notifyItemRangeInserted(0, newEvents.size)
        } else if (newEvents.size > oldItemCount) {
            // Jika ada data baru yang ditambahkan
            notifyItemRangeInserted(oldItemCount, newEvents.size - oldItemCount)
        } else {
            // Jika hanya beberapa item yang berubah, kita bisa mengupdate secara spesifik
            notifyItemRangeChanged(0, newEvents.size)
        }
    }
}
