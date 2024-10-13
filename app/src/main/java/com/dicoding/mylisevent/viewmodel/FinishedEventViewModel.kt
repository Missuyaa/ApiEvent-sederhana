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

class FinishedEventViewModel : ViewModel() {

    private val _finishedEvents = MutableLiveData<List<Event>>()
    val finishedEvents: LiveData<List<Event>> get() = _finishedEvents

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchFinishedEvents() {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.apiService.getFinishedEvents()
                if (response.isSuccessful) {
                    val finishedEvents = response.body()?.listEvents ?: emptyList()
                    withContext(Dispatchers.Main) {
                        Log.d("FinishedEventViewModel", "Received ${finishedEvents.size} finished events")
                        _finishedEvents.value = finishedEvents
                        _isLoading.value = false

                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _errorMessage.value = "Failed to load finished events"
                        _isLoading.value = false

                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "Error: ${e.message}"
                    _isLoading.value = false

                }
            }
        }
    }

}

