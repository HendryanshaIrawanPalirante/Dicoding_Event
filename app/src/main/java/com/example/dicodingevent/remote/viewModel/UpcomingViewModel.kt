package com.example.dicodingevent.remote.viewModel

import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.EventsRepository

class UpcomingViewModel(private val eventsRepository: EventsRepository): ViewModel() {

    fun getUpcomingEvents(activeEvents: Int) = eventsRepository.getEvents(activeEvents)

}