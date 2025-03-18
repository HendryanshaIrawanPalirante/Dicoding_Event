package com.example.dicodingevent.remote.apiService

import com.example.dicodingevent.remote.response.DetailEventResponse
import com.example.dicodingevent.remote.response.EventResponse
import com.example.dicodingevent.remote.response.EventSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/events")
    suspend fun getEvents(
        @Query("active") active: Int
    ): EventResponse

    @GET("/events/{id}")
    suspend fun getDetailEvent(
        @Path("id") id: Int
    ): DetailEventResponse

    @GET("/events")
    suspend fun getEventsSearch(
        @Query("active") active: Int,
        @Query("q") name: String
    ): EventSearchResponse

}