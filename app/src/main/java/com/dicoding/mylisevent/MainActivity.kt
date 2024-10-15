package com.dicoding.mylisevent

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = supportFragmentManager
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_upcoming -> UpcomingFragment()
                R.id.nav_finished -> FinishedFragment()
                else -> return@setOnItemSelectedListener false
            }
            loadFragment(selectedFragment)
            true
        }

        // Default fragment
        loadFragment(UpcomingFragment())
        bottomNavigationView.selectedItemId = R.id.nav_upcoming // Set default selected item
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()

        Log.d("MainActivity", "Fragment ${fragment.javaClass.simpleName} loaded")
    }

    fun loadEventDetail(eventId: String) {
        Log.d("MainActivity", "Loading event detail for ID: $eventId")

        val eventDetailFragment = EventDetailFragment().apply {
            arguments = Bundle().apply {
                putString("EVENT_ID", eventId)
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, eventDetailFragment)
            .addToBackStack(null)
            .commit()
    }
}
