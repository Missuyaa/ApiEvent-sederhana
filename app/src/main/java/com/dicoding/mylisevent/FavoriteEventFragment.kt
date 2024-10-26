package com.dicoding.mylisevent

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mylisevent.viewmodel.FavoriteEventViewModel

class FavoriteEventFragment : Fragment() {

    private val favoriteEventViewModel: FavoriteEventViewModel by viewModels()
    private lateinit var favoriteAdapter: FavoriteEventAdapter
    private var progressBar: ProgressBar? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progressBar)
        recyclerView = view.findViewById(R.id.favoriteRecyclerView)

        progressBar?.visibility = View.VISIBLE
        recyclerView?.visibility = View.GONE

        favoriteAdapter = FavoriteEventAdapter { event ->
            val fragment = EventDetailFragment.newInstance(event.id.toString())
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteAdapter
        }

        // Observe data LiveData untuk favorit
        favoriteEventViewModel.allFavorites.observe(viewLifecycleOwner) { favoriteEvents ->
            Handler(Looper.getMainLooper()).postDelayed({
                progressBar?.visibility = View.GONE
                recyclerView?.visibility = View.VISIBLE
                favoriteAdapter.submitList(favoriteEvents)
            }, 2000) // 2 detik delay
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        progressBar = null
        recyclerView = null
    }
}
