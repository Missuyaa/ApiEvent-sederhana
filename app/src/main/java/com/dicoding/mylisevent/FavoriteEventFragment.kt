package com.dicoding.mylisevent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mylisevent.viewmodel.FavoriteEventViewModel

class FavoriteEventFragment : Fragment() {

    private val favoriteEventViewModel: FavoriteEventViewModel by viewModels()
    private lateinit var favoriteAdapter: FavoriteEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.favoriteRecyclerView)
        favoriteAdapter = FavoriteEventAdapter { event ->
            // Navigasi ke halaman detail event saat item diklik
            val fragment = EventDetailFragment.newInstance(event.id.toString())
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }


        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteAdapter
        }

        // Observe data favorites
        favoriteEventViewModel.allFavorites.observe(viewLifecycleOwner) { favoriteEvents ->
            favoriteAdapter.submitList(favoriteEvents)
        }

    }
}
