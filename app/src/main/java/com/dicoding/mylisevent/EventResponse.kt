package com.dicoding.mylisevent

import com.dicoding.mylisevent.model.Event

data class EventResponse(
        val error: Boolean,
        val message: String,
        val listEvents: List<Event>
    )