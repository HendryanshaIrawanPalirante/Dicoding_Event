package com.example.dicodingevent.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.dicodingevent.data.room.EventDao
import com.example.dicodingevent.remote.apiService.ApiService
import com.example.dicodingevent.remote.response.DetailEventResponse
import com.example.dicodingevent.remote.response.ListEventsItem
import com.example.dicodingevent.remote.response.ListEventsSearchItem
import com.example.dicodingevent.data.entity.Event
import kotlin.concurrent.Volatile

class EventsRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao
) {

    fun getEvents(activeEvent: Int): LiveData<Result<List<ListEventsItem?>?>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEvents(activeEvent)
            val finishedEvents = response.listEvents
            emit(Result.Success(finishedEvents))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getDetailEvent(id: Int?): LiveData<Result<DetailEventResponse?>> = liveData {
        emit(Result.Loading)
        try {
            val response = id?.let { apiService.getDetailEvent(it) }
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun searchEvents(activeEvents: Int, name: String): LiveData<Result<List<ListEventsSearchItem?>?>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEventsSearch(activeEvents, name)
            val events = response.listEvents
            emit(Result.Success(events))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun setFavoriteEvent(event: Event) {
        eventDao.insert(event)
    }

    fun getFavoriteEvent(): LiveData<List<Event>> {
        return eventDao.getAllEvent()
    }

    fun isFavorite(id: Int): LiveData<Boolean> {
        return eventDao.isFavorite(id)
    }

    suspend fun delete(event: Event) {
        eventDao.delete(event)
    }

    companion object {
        @Volatile
        private var instance: EventsRepository? = null

        fun getInstance(apiService: ApiService, eventDao: EventDao): EventsRepository =
            instance ?: synchronized(this) {
                instance ?: EventsRepository(apiService, eventDao)
            }.also { instance = it }
    }

}