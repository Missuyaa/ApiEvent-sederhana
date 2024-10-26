package com.dicoding.mylisevent.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorite_event")
data class FavoriteEvent(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val imageLogo: String,
    val category: String,
    val ownerName: String,
    val cityName: String,
    val quota: Int,
    val registrants: Int,
    val beginTime: String,
    val endTime: String,
    val link: String
)