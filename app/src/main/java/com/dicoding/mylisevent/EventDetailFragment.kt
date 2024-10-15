package com.dicoding.mylisevent

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.mylisevent.api.ApiClient
import com.dicoding.mylisevent.viewmodel.EventDetailViewModel
import kotlinx.coroutines.launch

class EventDetailFragment : Fragment() {

    private lateinit var eventId: String
    private val viewModel: EventDetailViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mengambil event ID dari arguments
        arguments?.let {
            eventId = it.getString("EVENT_ID") ?: ""
            Log.d("EventDetailFragment", "Event ID: $eventId")
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

        viewModel.eventDetail.observe(viewLifecycleOwner) { eventDetails ->
            Log.d("EventDetailFragment", "Event details received: $eventDetails")

            view.findViewById<TextView>(R.id.name_event).text = eventDetails.name
            view.findViewById<TextView>(R.id.owner_event).text = eventDetails.ownerName
            view.findViewById<TextView>(R.id.time_event).text = eventDetails.beginTime
            view.findViewById<TextView>(R.id.quota_event).text =
                "${eventDetails.quota - eventDetails.registrants} spots left"

            val processedDescription = removeImageTags(eventDetails.description)
            val descriptionSpanned = fromHtml(processedDescription)
            view.findViewById<TextView>(R.id.description_event).text = descriptionSpanned

            Glide.with(requireContext()).load(eventDetails.imageLogo)
                .into(view.findViewById<ImageView>(R.id.image_logo))

            val registerButton = view.findViewById<Button>(R.id.button_register)
            registerButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(eventDetails.link)
                startActivity(intent)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            view.findViewById<TextView>(R.id.description_event).text = errorMessage
        }

        viewModel.fetchEventDetails(eventId)
    }

    companion object {
        @JvmStatic
        fun newInstance(eventId: String) = EventDetailFragment().apply {
            arguments = Bundle().apply {
                putString("EVENT_ID", eventId)
            }
        }
    }

    private fun removeImageTags(description: String): String {
        // Remove all img tags from the description
        return description.replace("<img[^>]*>".toRegex(), "")
    }

    private fun fromHtml(html: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(html)
        }
    }
}