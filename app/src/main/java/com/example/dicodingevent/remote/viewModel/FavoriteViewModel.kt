package com.example.dicodingevent.remote.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.EventsRepository
import com.example.dicodingevent.data.entity.Event
import kotlinx.coroutines.launch

class FavoriteViewModel(private val eventRepository: EventsRepository): ViewModel() {

    fun getEventFavorite() = eventRepository.getFavoriteEvent()

    fun getIsFavorite(id: Int) = eventRepository.isFavorite(id)

    fun saveEvent(event: Event) {
        viewModelScope.launch {
            eventRepository.setFavoriteEvent(event)
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            eventRepository.delete(event)
        }
    }

}