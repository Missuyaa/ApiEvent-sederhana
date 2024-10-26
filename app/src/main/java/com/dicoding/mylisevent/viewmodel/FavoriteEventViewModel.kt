package com.dicoding.mylisevent.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.mylisevent.database.AppDatabase
import com.dicoding.mylisevent.model.FavoriteEvent
import kotlinx.coroutines.launch

class FavoriteEventViewModel(application: Application) : AndroidViewModel(application) {
    private val favoriteEventDao = AppDatabase.getDatabase(application).favoriteEventDao()

    // LiveData untuk semua event favorit
    val allFavorites: LiveData<List<FavoriteEvent>> = favoriteEventDao.getAllFavorites()

    // Menambahkan event ke favorit
    fun addFavorite(event: FavoriteEvent) = viewModelScope.launch {
        favoriteEventDao.insertFavorite(event)
    }

    // Menghapus event dari favorit
    fun removeFavorite(eventId: Int) = viewModelScope.launch {
        favoriteEventDao.deleteFavorite(eventId)
    }

    // Mengecek apakah suatu event adalah favorit
    fun isFavorite(eventId: Int): LiveData<FavoriteEvent?> {
        return favoriteEventDao.isFavorite(eventId)
    }
}
