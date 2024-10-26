package com.dicoding.mylisevent.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.dicoding.mylisevent.database.AppDatabase
import com.dicoding.mylisevent.model.FavoriteEvent

class FavoriteEventViewModel(application: Application) : AndroidViewModel(application) {
    private val favoriteEventDao = AppDatabase.getDatabase(application).favoriteEventDao()

    val allFavorites: LiveData<List<FavoriteEvent>> = favoriteEventDao.getAllFavorites()

    fun addFavorite(event: FavoriteEvent) = viewModelScope.launch {
        favoriteEventDao.insertFavorite(event)
    }

    fun removeFavorite(eventId: Int) = viewModelScope.launch {
        favoriteEventDao.deleteFavorite(eventId)
    }
}
