package com.dicoding.mylisevent

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPrefs = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val isDarkMode = sharedPrefs.getBoolean("dark_mode", false)

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_upcoming -> UpcomingFragment()
                R.id.nav_finished -> FinishedFragment()
                R.id.nav_favorite -> FavoriteEventFragment()
                R.id.nav_setting -> SettingsFragment()
                else -> return@setOnItemSelectedListener false
            }
            loadFragment(selectedFragment)
            true
        }

        // Cek jika savedInstanceState bukan null, jangan muat ulang fragment default
        if (savedInstanceState == null) {
            loadFragment(UpcomingFragment())
            bottomNavigationView.selectedItemId = R.id.nav_upcoming
        }
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
