package com.example.dicodingevent.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.adapter.ListEventAdapter
import com.example.dicodingevent.databinding.FragmentFavoriteBinding
import com.example.dicodingevent.remote.response.ListEventsItem
import com.example.dicodingevent.remote.viewModel.FavoriteViewModel
import com.example.dicodingevent.remote.viewModel.MainViewModelFactory
import com.example.dicodingevent.ui.activity.DetailEventActivity

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvEvent.setHasFixedSize(true)
        binding.rvEvent.layoutManager = LinearLayoutManager(requireContext())

        val factory: MainViewModelFactory = MainViewModelFactory.getInstance(requireActivity())
        val favoriteViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        favoriteViewModel.getEventFavorite().observe(viewLifecycleOwner) { eventFavorite ->
            val items = arrayListOf<ListEventsItem>()
            eventFavorite.map {
                val item = ListEventsItem(id = it.id, name = it.name, mediaCover = it.mediaCover)
                items.add(item)
            }
            if (items.isEmpty()) {
                binding.tvFavorite.visibility = View.VISIBLE
            }
            setRecyclerView(items)
        }

    }

    private fun setRecyclerView(listEvent: List<ListEventsItem?>?) {
        val listEventAdapter = ListEventAdapter(listEvent)
        binding.rvEvent.adapter = listEventAdapter

        listEventAdapter.setOnItemClickCallback(object : ListEventAdapter.OnItemClickCallback {
            override fun onItemClicked(listEventsItem: ListEventsItem?) {
                val moveToDetailEvent = Intent(requireContext(), DetailEventActivity::class.java)
                moveToDetailEvent.putExtra(DetailEventActivity.EXTRA_ID, listEventsItem?.id)
                startActivity(moveToDetailEvent)
            }
        })
    }

}