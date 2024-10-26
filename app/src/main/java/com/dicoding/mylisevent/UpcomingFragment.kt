package com.dicoding.mylisevent

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mylisevent.viewmodel.UpcomingEventViewModel

class UpcomingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: View
    private lateinit var viewModel: UpcomingEventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upcoming, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)

        viewModel = ViewModelProvider(this)[UpcomingEventViewModel::class.java]

        setupRecyclerView()
        observeLiveData()

        return view
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = EventAdapter(emptyList()) { event ->
            (activity as? MainActivity)?.loadEventDetail(event.id.toString())
        }
    }

    private fun observeLiveData() {
        // Menggunakan viewLifecycleOwnerLiveData untuk menambahkan observer secara aman
        viewLifecycleOwnerLiveData.observe(this) { viewLifecycleOwner ->
            if (viewLifecycleOwner != null) {
                progressBar.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE

                // Tambahkan delay 2 detik untuk simulasi loading
                Handler(Looper.getMainLooper()).postDelayed({
                    viewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
                        progressBar.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        (recyclerView.adapter as EventAdapter).updateData(events)
                        if (events.isEmpty()) {
                            Toast.makeText(context, "No upcoming events", Toast.LENGTH_SHORT).show()
                        }
                    }

                    viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
                        progressBar.visibility = View.GONE
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    }

                    viewModel.fetchUpcomingEvents()
                }, 2000) // 2 detik delay
            }
        }
    }
}
