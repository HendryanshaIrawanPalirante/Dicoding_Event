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
import com.example.dicodingevent.databinding.FragmentFinishedEventBinding
import com.example.dicodingevent.remote.response.ListEventsItem
import com.example.dicodingevent.remote.viewModel.EventSearchViewModel
import com.example.dicodingevent.remote.viewModel.FinishedViewModel
import com.example.dicodingevent.remote.viewModel.MainViewModelFactory
import com.example.dicodingevent.ui.activity.DetailEventActivity

class FinishedEventFragment : Fragment() {

    private var _binding: FragmentFinishedEventBinding? = null
    private val binding get() = _binding!!
    private val activeEvent = 0
    private lateinit var finishedViewModel: FinishedViewModel
    private lateinit var eventSearchViewModel: EventSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvEvent.setHasFixedSize(true)
        binding.rvEvent.layoutManager = LinearLayoutManager(requireContext())

        val factory: MainViewModelFactory = MainViewModelFactory.getInstance(requireActivity())
        finishedViewModel = ViewModelProvider(this, factory)[FinishedViewModel::class.java]
        eventSearchViewModel = ViewModelProvider(this, factory)[EventSearchViewModel::class.java]

        getFinishedEvents()
        setSearch()

    }

    private fun getFinishedEvents() {
        finishedViewModel.getFinishedEvents(activeEvent).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val data = result.data
                        if (data != null) {
                            setRecyclerView(data)
                        }
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

    private fun setSearch() {
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
        binding.progressBarFinishedEvent.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorMessage(errorMessage: String) {
        Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}