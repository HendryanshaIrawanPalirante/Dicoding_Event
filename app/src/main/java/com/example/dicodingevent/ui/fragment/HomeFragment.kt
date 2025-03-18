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
import com.example.dicodingevent.adapter.HomeEventAdapter
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.databinding.FragmentHomeBinding
import com.example.dicodingevent.remote.response.ListEventsItem
import com.example.dicodingevent.remote.viewModel.HomeViewModel
import com.example.dicodingevent.remote.viewModel.MainViewModelFactory
import com.example.dicodingevent.ui.activity.DetailEventActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val upcomingEvent = 1
    private val finishedEvent = 0
    private lateinit var homeEventViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factor: MainViewModelFactory = MainViewModelFactory.getInstance(requireActivity())
        homeEventViewModel = ViewModelProvider(this, factor)[HomeViewModel::class.java]

        getUpcomingEvents()
        getFinishedEvents()

    }

    private fun getUpcomingEvents() {
        homeEventViewModel.getUpcomingEvents(upcomingEvent).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoadingUpcomingEvent(true)
                    }
                    is Result.Success -> {
                        showLoadingUpcomingEvent(false)
                        val data = result.data
                        if(data == null) {
                            binding.tvUpcomingEvent.visibility = View.VISIBLE
                        }
                        setRecyclerUpcomingView(data)
                    }
                    is Result.Error -> {
                        showLoadingUpcomingEvent(false)
                        showErrorMessage(result.error)
                    }
                }
            }
        }
    }

    private fun getFinishedEvents() {
        homeEventViewModel.getFinishedEvents(finishedEvent).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoadingFinishedEvent(true)
                    }
                    is Result.Success -> {
                        showLoadingFinishedEvent(false)
                        val data = result.data
                        if(data == null) {
                            binding.tvFinishedEvent.visibility = View.VISIBLE
                        }
                        setRecyclerFinishedView(data)
                    }
                    is Result.Error -> {
                        showLoadingFinishedEvent(false)
                        showErrorMessage(result.error)
                    }
                }
            }
        }
    }

    private fun setRecyclerUpcomingView(listEvent: List<ListEventsItem?>?) {
        binding.rvUpcomingEvent.setHasFixedSize(true)
        binding.rvUpcomingEvent.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        val limitEventList = listEvent?.take(5)
        val homeEventAdapter = HomeEventAdapter(limitEventList)
        binding.rvUpcomingEvent.adapter = homeEventAdapter

        homeEventAdapter.setOnItemCLickCallback(object : HomeEventAdapter.OnItemClickCallback {
            override fun onItemClicked(listEventsItem: ListEventsItem?) {
                val moveToDetailEvent = Intent(requireActivity(), DetailEventActivity::class.java)
                moveToDetailEvent.putExtra(DetailEventActivity.EXTRA_ID, listEventsItem?.id)
                startActivity(moveToDetailEvent)
            }
        })
    }

    private fun setRecyclerFinishedView(listEvent: List<ListEventsItem?>?) {
        binding.rvFinishedEvent.setHasFixedSize(true)
        binding.rvFinishedEvent.layoutManager = LinearLayoutManager(requireActivity())
        val limitEventList = listEvent?.take(5)
        val homeEventAdapter = HomeEventAdapter(limitEventList)
        binding.rvFinishedEvent.adapter = homeEventAdapter

        homeEventAdapter.setOnItemCLickCallback(object : HomeEventAdapter.OnItemClickCallback {
            override fun onItemClicked(listEventsItem: ListEventsItem?) {
                val moveToDetailEvent = Intent(requireActivity(), DetailEventActivity::class.java)
                moveToDetailEvent.putExtra(DetailEventActivity.EXTRA_ID, listEventsItem?.id)
                startActivity(moveToDetailEvent)
            }
        })
    }

    private fun showLoadingUpcomingEvent(isLoading: Boolean) {
        binding.progressBarUpcomingEvent.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showLoadingFinishedEvent(isLoading: Boolean) {
        binding.progressBarFinishedEvent.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorMessage(errorMessage: String) {
        Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_LONG).show()
    }

}