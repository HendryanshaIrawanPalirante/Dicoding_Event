package com.example.dicodingevent.remote.viewModel

import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.EventsRepository

class HomeViewModel(private val eventsRepository: EventsRepository): ViewModel() {

    fun getFinishedEvents(activeEvents: Int) = eventsRepository.getEvents(activeEvents)
    fun getUpcomingEvents(activeEvents: Int) = eventsRepository.getEvents(activeEvents)

}