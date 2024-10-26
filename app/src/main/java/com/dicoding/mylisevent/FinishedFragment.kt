package com.dicoding.mylisevent

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mylisevent.viewmodel.FinishedEventViewModel

class FinishedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: View
    private lateinit var viewModel: FinishedEventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("FinishedFragment", "onCreateView called")
        val view = inflater.inflate(R.layout.fragment_finished, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)

        viewModel = ViewModelProvider(this)[FinishedEventViewModel::class.java]

        setupRecyclerView()
        observeLiveData()

        return view
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = EventAdapter(emptyList()) { event ->
            // Implementasi ketika event diklik
            (activity as? MainActivity)?.loadEventDetail(event.id.toString())
        }
    }

    private fun observeLiveData() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        viewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            Handler(Looper.getMainLooper()).postDelayed({
                recyclerView.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                (recyclerView.adapter as EventAdapter).updateData(events)
                if (events.isEmpty()) {
                    Toast.makeText(context, "No upcoming events", Toast.LENGTH_SHORT).show()
                }
            }, 2000) // 2 detik delay
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Handler(Looper.getMainLooper()).postDelayed({
                recyclerView.visibility = View.VISIBLE
                progressBar.visibility = View.GONE

                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }, 2000) // 2 detik delay
        }
        viewModel.fetchFinishedEvents()
    }
}
