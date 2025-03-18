package com.example.dicodingevent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.dicodingevent.databinding.ItemRowEventHomeBinding
import com.example.dicodingevent.remote.response.ListEventsItem

class HomeEventAdapter(private val listEvent: List<ListEventsItem?>?): RecyclerView.Adapter<HomeEventAdapter.ListViewHolder>() {

    class ListViewHolder(val binding: ItemRowEventHomeBinding): ViewHolder(binding.root)

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemCLickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemRowEventHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listEvent?.get(position)
        holder.binding.tvTitle.text = data?.name
        Glide.with(holder.itemView.context)
            .load(data?.mediaCover)
            .into(holder.binding.imgItemPhoto)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listEvent?.get(holder.adapterPosition))
        }
    }

    override fun getItemCount(): Int {
        return listEvent?.size ?: 0
    }

    interface OnItemClickCallback {
        fun onItemClicked(listEventsItem: ListEventsItem?)
    }

}