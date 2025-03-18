package com.example.dicodingevent.remote.viewModel

import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.EventsRepository

class DetailEventViewModel(private val eventsRepository: EventsRepository): ViewModel() {

    fun getDetailEvent(id: Int?) = eventsRepository.getDetailEvent(id)

}