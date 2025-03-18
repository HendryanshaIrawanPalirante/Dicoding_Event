package com.example.dicodingevent.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.adapter.ListEventAdapter
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.databinding.FragmentUpcomingEventBinding
import com.example.dicodingevent.remote.response.ListEventsItem
import com.example.dicodingevent.remote.viewModel.EventSearchViewModel
import com.example.dicodingevent.remote.viewModel.MainViewModelFactory
import com.example.dicodingevent.remote.viewModel.UpcomingViewModel
import com.example.dicodingevent.ui.activity.DetailEventActivity

class UpcomingEventFragment : Fragment() {

    private var _binding: FragmentUpcomingEventBinding? = null
    private val binding get() = _binding!!
    private val activeEvent = 1
    private lateinit var upcomingEventViewModel: UpcomingViewModel
    private lateinit var eventSearchViewModel: EventSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvEvent.setHasFixedSize(true)
        binding.rvEvent.layoutManager = LinearLayoutManager(requireContext())

        val factory: MainViewModelFactory = MainViewModelFactory.getInstance(requireActivity())
        upcomingEventViewModel = ViewModelProvider(this, factory)[UpcomingViewModel::class.java]
        eventSearchViewModel = ViewModelProvider(this, factory)[EventSearchViewModel::class.java]

        getUpcomingEvents()
        setSerach()

    }

    private fun getUpcomingEvents() {
        upcomingEventViewModel.getUpcomingEvents(activeEvent).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val data = result.data
                        setRecyclerView(data)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showErrorMessage(result.error)
                    }
                }
            }
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

    private fun setSerach() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    searchBar.setText("")
                    searchEvents(searchView.text.toString())
                    false
                }
        }
    }

    private fun searchEvents(name: String) {
        eventSearchViewModel.searchEvents(activeEvent, name).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val data = result.data
                        if(data == null) {
                            binding.tvEvent.visibility = View.VISIBLE
                        }
                        val convertedResult = data?.map {
                            ListEventsItem(
                                id = it?.id,
                                name = it?.name,
                                mediaCover = it?.mediaCover
                            )
                        }
                        setRecyclerView(convertedResult)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showErrorMessage(result.error)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarUpcomingEvent.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorMessage(errorMessage: String) {
        Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}