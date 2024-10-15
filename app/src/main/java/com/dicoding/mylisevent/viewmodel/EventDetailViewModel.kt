package com.dicoding.mylisevent.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mylisevent.EventDetailResponse
import com.dicoding.mylisevent.api.ApiClient
import com.dicoding.mylisevent.model.Event
import kotlinx.coroutines.launch
import retrofit2.Response

class EventDetailViewModel : ViewModel() {

    private val _eventDetail = MutableLiveData<Event>()
    val eventDetail: LiveData<Event> get() = _eventDetail

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchEventDetails(eventId: String) {
        viewModelScope.launch {
            try {
                val response: Response<EventDetailResponse> = ApiClient.apiService.getEventDetails(eventId)
                if (response.isSuccessful) {
                    val eventDetailResponse = response.body()
                    eventDetailResponse?.let {
                        if (!it.error) {
                            _eventDetail.postValue(it.event)
                            Log.d("EventDetailViewModel", "Event details fetched: ${it.event}")
                        } else {
                            _error.postValue(it.message)
                        }
                    } ?: run {
                        _error.postValue("Response body is null")
                    }
                } else {
                    _error.postValue("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Failed to load event details: ${e.message}")
            }
        }
    }
}