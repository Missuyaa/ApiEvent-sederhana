package com.dicoding.mylisevent.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.mylisevent.model.FavoriteEvent

@Dao
interface FavoriteEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(event: FavoriteEvent)

    @Query("DELETE FROM favorite_event WHERE id = :eventId")
    suspend fun deleteFavorite(eventId: Int)

    @Query("SELECT * FROM favorite_event")
    fun getAllFavorites(): LiveData<List<FavoriteEvent>>

    // Add this query to check if an event is favorited
    @Query("SELECT * FROM favorite_event WHERE id = :eventId LIMIT 1")
    fun isFavorite(eventId: Int): LiveData<FavoriteEvent?>
}
