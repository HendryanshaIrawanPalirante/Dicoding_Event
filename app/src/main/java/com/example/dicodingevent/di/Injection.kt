package com.example.dicodingevent.di

import android.content.Context
import com.example.dicodingevent.data.EventsRepository
import com.example.dicodingevent.data.room.EventRoomDatabase
import com.example.dicodingevent.remote.apiService.ApiConfig

object Injection {

    fun provideRepository(context: Context): EventsRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventRoomDatabase.getDatabase(context)
        val dao = database.eventDao()
        return EventsRepository.getInstance(apiService, dao)
    }

}