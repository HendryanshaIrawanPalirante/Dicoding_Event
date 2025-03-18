package com.example.dicodingevent.remote.response

import com.google.gson.annotations.SerializedName

data class EventSearchResponse(

	@field:SerializedName("listEvents")
	val listEvents: List<ListEventsSearchItem?>? = null,

)

data class ListEventsSearchItem(

	@field:SerializedName("mediaCover")
	val mediaCover: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

)
