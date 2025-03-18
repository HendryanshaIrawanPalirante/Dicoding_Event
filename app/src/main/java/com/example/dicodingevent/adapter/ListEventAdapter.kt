package com.example.dicodingevent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.databinding.ItemRowEventBinding
import com.example.dicodingevent.remote.response.ListEventsItem

class ListEventAdapter(private val listEvent: List<ListEventsItem?>?): RecyclerView.Adapter<ListEventAdapter.ListViewHolder>() {

    class ListViewHolder(val binding: ItemRowEventBinding): RecyclerView.ViewHolder(binding.root)

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listEvent?.size ?: 0
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

    interface OnItemClickCallback {
        fun onItemClicked(listEventsItem: ListEventsItem?)
    }

}