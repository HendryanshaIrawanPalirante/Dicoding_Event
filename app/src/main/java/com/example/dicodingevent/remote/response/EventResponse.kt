package com.example.dicodingevent.remote.response

import com.google.gson.annotations.SerializedName

data class EventResponse(

	@field:SerializedName("listEvents")
	val listEvents: List<ListEventsItem?>? = null,

)

data class ListEventsItem(

	@field:SerializedName("mediaCover")
	val mediaCover: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)