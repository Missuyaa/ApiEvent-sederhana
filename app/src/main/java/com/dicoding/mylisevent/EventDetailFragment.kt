package com.dicoding.mylisevent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.mylisevent.api.ApiClient
import kotlinx.coroutines.launch

class EventDetailFragment : Fragment() {

    private lateinit var eventId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mengambil event ID dari arguments
        arguments?.let {
            eventId = it.getString("EVENT_ID") ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout untuk fragment event detail
        return inflater.inflate(R.layout.fragment_event_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Panggil fungsi untuk mendapatkan detail event
        fetchEventDetails(view)
    }


    private fun fetchEventDetails(view: View) {
        lifecycleScope.launch {
            try {
                val eventDetails = ApiClient.apiService.getEventDetails(eventId)
                // Update UI dengan detail event yang diperoleh dari API
                view.findViewById<TextView>(R.id.name_event).text = eventDetails.name
                view.findViewById<TextView>(R.id.owner_event).text = eventDetails.ownerName
                view.findViewById<TextView>(R.id.time_event).text = eventDetails.beginTime
                view.findViewById<TextView>(R.id.quota_event).text =
                    "${eventDetails.quota - eventDetails.registrants} spots left"
                view.findViewById<TextView>(R.id.description_event).text = eventDetails.description
                Glide.with(requireContext()).load(eventDetails.imageLogo)
                    .into(view.findViewById<ImageView>(R.id.image_logo))

                // Setup button untuk membuka link event
                val registerButton = view.findViewById<Button>(R.id.button_register)
                registerButton.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(eventDetails.link)
                    startActivity(intent)
                }
            } catch (e: Exception) {
                // Tampilkan pesan error jika gagal mendapatkan data
                view.findViewById<TextView>(R.id.description_event).text =
                    "Failed to load event details"
            }
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(eventId: String) = EventDetailFragment().apply {
            arguments = Bundle().apply {
                putString("EVENT_ID", eventId)
            }
        }
    }
}