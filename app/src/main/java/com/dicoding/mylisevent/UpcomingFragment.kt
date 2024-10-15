package com.dicoding.mylisevent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

//        val view = inflater.inflate(R.layout.fragment_simple, container, false)
//        val textView = view.findViewById<TextView>(R.id.textView)
//        textView.text = "Ini Upcoming Fragment"
//        return view
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = EventAdapter(emptyList()) { event ->
            // Implementasi ketika event diklik
            Log.d("UpcomingFragment", "Event clicked: ${event.id}")

            (activity as? MainActivity)?.loadEventDetail(event.id.toString())
            Log.d("UpcomingFragment", "Event clicked: ${event.name}")
        }
    }

    private fun observeLiveData() {
        // Observe LiveData untuk event yang akan datang
        viewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            (recyclerView.adapter as EventAdapter).updateData(events)
            Log.d("UpcomingFragment", "Displaying ${events.size} upcoming events")
        }

        // Observe LiveData untuk pesan error
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            Log.e("UpcomingFragment", "Error: $message")
        }

        // Fetch data event yang akan datang
        viewModel.fetchUpcomingEvents()
    }
}
