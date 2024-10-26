package com.dicoding.mylisevent

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment() {

    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Periksa preferensi yang tersimpan untuk tema dan atur sebelum tampilan dibuat
        val sharedPrefs = requireActivity().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val isDarkMode = sharedPrefs.getBoolean("dark_mode", false)

        // Atur tema berdasarkan preferensi yang tersimpan
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progressBar)
        val switchDarkMode = view.findViewById<Switch>(R.id.darkModeSwitch)
        val sharedPrefs = requireActivity().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val isDarkMode = sharedPrefs.getBoolean("dark_mode", false)

        // Atur switch berdasarkan preferensi yang tersimpan
        switchDarkMode.isChecked = isDarkMode

        // Ubah tema saat switch diaktifkan atau dinonaktifkan
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            progressBar?.visibility = View.VISIBLE

            // Tambahkan delay 2 detik untuk simulasi loading
            Handler(Looper.getMainLooper()).postDelayed({
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

                // Simpan preferensi tema
                with(sharedPrefs.edit()) {
                    putBoolean("dark_mode", isChecked)
                    apply()
                }

                progressBar?.visibility = View.GONE
            }, 2000) // 2 detik delay
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        progressBar = null // Menghindari memory leak dengan menghapus referensi ke ProgressBar
    }
}
