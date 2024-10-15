package com.dicoding.mylisevent

import com.dicoding.mylisevent.model.Event

data class EventDetailResponse(
    val error: Boolean,
    val message: String,
    val event: Event
)
