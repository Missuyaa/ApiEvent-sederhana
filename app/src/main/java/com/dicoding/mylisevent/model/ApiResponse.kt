package com.dicoding.mylisevent.model

data class ApiResponse(
    val error: Boolean,
    val message: String,
    val listEvents: List<Event>
)
