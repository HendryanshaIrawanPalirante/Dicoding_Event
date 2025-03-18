package com.example.dicodingevent.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.entity.Event
import com.example.dicodingevent.databinding.ActivityDetailEventBinding
import com.example.dicodingevent.remote.response.DetailEvent
import com.example.dicodingevent.remote.viewModel.DetailEventViewModel
import com.example.dicodingevent.remote.viewModel.FavoriteViewModel
import com.example.dicodingevent.remote.viewModel.MainViewModelFactory

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var detailEventViewModel: DetailEventViewModel

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: MainViewModelFactory = MainViewModelFactory.getInstance(this@DetailEventActivity)
        detailEventViewModel = ViewModelProvider(this, factory)[DetailEventViewModel::class.java]
        favoriteViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        val idEvent = intent?.getIntExtra(EXTRA_ID, 0)
        getDetailEvent(idEvent)



    }

    private fun getDetailEvent(idEvent: Int?) {

        detailEventViewModel.getDetailEvent(idEvent).observe(this@DetailEventActivity) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val data = result.data?.event
                        if (data != null) {
                            setData(data)
                            val favoriteData = Event(
                                name = data.name,
                                id = data.id,
                                mediaCover = data.mediaCover,
                                isFavorite = true
                            )
                            setFavoriteEvent(favoriteData)
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

    private fun setData(data: DetailEvent) {
        with(binding) {
            tvTitle.text = data.name
            tvBegin.text = data.beginTime
            tvOwner.text = data.ownerName
            val quotaRemaining = ((data.quota ?: 0) - (data.registrants ?: 0)).toString()
            tvQuota.text = quotaRemaining
            description.text = HtmlCompat.fromHtml(
                data.description.toString(),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            Glide.with(this@DetailEventActivity)
                .load(data.mediaCover)
                .into(imageDetail)

            binding.btnRegister.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.link))
                startActivity(intent)
            }
        }
    }

    private fun setFavoriteEvent(data: Event) {
        saveEventToFavorite(data)
    }

    private fun saveEventToFavorite(data: Event) {
        data.id?.let {
            favoriteViewModel.getIsFavorite(it).observe(this) { result ->
                if (result){
                    binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_24)
                    binding.btnFavorite.setOnClickListener {
                        favoriteViewModel.deleteEvent(data)
                    }
                }else {
                    binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                    binding.btnFavorite.setOnClickListener {
                        favoriteViewModel.saveEvent(data)
                    }
                }
            }
        }
    }



    private fun showLoading(isLoading: Boolean) {
        binding.progressBarDetailEvent.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorMessage(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

}