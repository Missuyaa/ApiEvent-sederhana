package com.dicoding.mylisevent.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mylisevent.api.ApiClient
import com.dicoding.mylisevent.model.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UpcomingEventViewModel : ViewModel() {

    private val _upcomingEvents = MutableLiveData<List<Event>>()
    val upcomingEvents: LiveData<List<Event>> get() = _upcomingEvents

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchUpcomingEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.apiService.getUpcomingEvents()
                if (response.isSuccessful) {
                    val upcomingEvents = response.body()?.listEvents ?: emptyList()
                    withContext(Dispatchers.Main) {
                        Log.d("UpcomingEventViewModel", "Received upcoming events: $upcomingEvents") // Logging data
                        _upcomingEvents.value = upcomingEvents
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _errorMessage.value = "Failed to load upcoming events"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "Error: ${e.message}"
                }
            }
        }
    }
}

