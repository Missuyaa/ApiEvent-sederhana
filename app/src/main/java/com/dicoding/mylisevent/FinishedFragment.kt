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

        // Inflate a simple layout with just a TextView
//        val view = inflater.inflate(R.layout.fragment_simple, container, false)
//        val textView = view.findViewById<TextView>(R.id.textView)
//        textView.text = "Ini Finished Fragment"
//        return view
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = EventAdapter(emptyList()) { event ->
            // Implementasi ketika event diklik
            (activity as? MainActivity)?.loadEventDetail(event.id.toString())
        }
    }

    private fun observeLiveData() {
        // Observe LiveData untuk event yang sudah selesai
        viewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            (recyclerView.adapter as EventAdapter).updateData(events)
            // Pastikan data event diterima
            if (events.isEmpty()) {
                Toast.makeText(context, "No finished events", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe LiveData untuk pesan error
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        // Fetch data event yang sudah selesai
        viewModel.fetchFinishedEvents()
    }
}
