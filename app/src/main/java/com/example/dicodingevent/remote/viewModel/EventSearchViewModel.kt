package com.example.dicodingevent.remote.viewModel

import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.EventsRepository

class EventSearchViewModel(private val eventsRepository: EventsRepository): ViewModel() {

    fun searchEvents(activeEvent: Int, name: String) = eventsRepository.searchEvents(activeEvent, name)

}