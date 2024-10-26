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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.dicoding.mylisevent.model.FavoriteEvent
import com.dicoding.mylisevent.viewmodel.EventDetailViewModel
import com.dicoding.mylisevent.viewmodel.FavoriteEventViewModel

class EventDetailFragment : Fragment() {

    private lateinit var eventId: String
    private val viewModel: EventDetailViewModel by viewModels()
    private val favoriteViewModel: FavoriteEventViewModel by viewModels()
    private lateinit var progressBar: ProgressBar

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
        progressBar = view.findViewById(R.id.progressBar)
        val favoriteButton = view.findViewById<ImageView>(R.id.favoriteButton)
        val registerButton = view.findViewById<Button>(R.id.button_register)

        progressBar.visibility = View.VISIBLE

        // Observasi detail event
        viewModel.eventDetail.observe(viewLifecycleOwner) { eventDetails ->
            progressBar.visibility = View.GONE
            if (eventDetails != null) {
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

                // Atur warna sesuai mode gelap/terang
                updateThemeColors(view, registerButton)

                registerButton.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(eventDetails.link)
                    startActivity(intent)
                }

                // Cek status favorit
                favoriteViewModel.isFavorite(eventId.toInt()).observe(viewLifecycleOwner) { favoriteEvent ->
                    val isFavorite = favoriteEvent != null
                    favoriteButton.setImageResource(
                        if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
                    )

                    // Aksi saat tombol favorit ditekan
                    favoriteButton.setOnClickListener {
                        if (isFavorite) {
                            favoriteViewModel.removeFavorite(eventId.toInt())
                        } else {
                            val eventToAdd = FavoriteEvent(
                                id = eventDetails.id,
                                name = eventDetails.name,
                                description = eventDetails.description,
                                imageLogo = eventDetails.imageLogo,
                                category = eventDetails.category,
                                ownerName = eventDetails.ownerName,
                                cityName = eventDetails.cityName,
                                quota = eventDetails.quota,
                                registrants = eventDetails.registrants,
                                beginTime = eventDetails.beginTime,
                                endTime = eventDetails.endTime,
                                link = eventDetails.link
                            )
                            favoriteViewModel.addFavorite(eventToAdd)
                        }
                    }
                }
            } else {
                view.findViewById<TextView>(R.id.description_event).text = "Event tidak ditemukan."
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            progressBar.visibility = View.GONE
            view.findViewById<TextView>(R.id.description_event).text = errorMessage
        }

        viewModel.fetchEventDetails(eventId)
    }

    private fun updateThemeColors(view: View, registerButton: Button) {
        if (isDarkModeEnabled()) {
            view.findViewById<TextView>(R.id.name_event).setTextColor(resources.getColor(R.color.white, null))
            registerButton.setBackgroundColor(resources.getColor(R.color.black, null))
        } else {
            view.findViewById<TextView>(R.id.name_event).setTextColor(resources.getColor(R.color.black, null))
            registerButton.setBackgroundColor(resources.getColor(R.color.teal_200, null))
        }
    }

    private fun removeImageTags(description: String): String {
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

    private fun isDarkModeEnabled(): Boolean {
        val currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES
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
