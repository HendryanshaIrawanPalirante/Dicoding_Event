package com.example.dicodingevent.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dicodingevent.data.entity.Event

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: Event)

    @Delete
    suspend fun delete(event: Event)

    @Query("SELECT * FROM events ORDER BY id ASC")
    fun getAllEvent(): LiveData<List<Event>>

    @Query("SELECT EXISTS(SELECT * FROM events WHERE id = :id AND isFavorite = 1)")
    fun isFavorite(id: Int): LiveData<Boolean>

}