package com.dicoding.mylisevent.api

import com.dicoding.mylisevent.EventDetailResponse
import com.dicoding.mylisevent.EventResponse
import com.dicoding.mylisevent.model.ApiResponse
import com.dicoding.mylisevent.model.Event
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response


interface ApiService {

    @GET("events?active=1") // API endpoint untuk event yang akan datang
    suspend fun getUpcomingEvents(): Response<ApiResponse>

    @GET("events?active=0")  // API endpoint untuk event yang sudah selesai
    suspend fun getFinishedEvents(): Response<ApiResponse>

    @GET("events/{id}")
    suspend fun getEventDetails(@Path("id") id: String): Response<EventDetailResponse>
}


