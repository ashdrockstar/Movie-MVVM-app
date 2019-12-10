package com.example.moviemvvmapp.single_movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.moviemvvmapp.R
import com.example.moviemvvmapp.data.api.POSTER_BASE_URL
import com.example.moviemvvmapp.data.api.TheMovieDBClient
import com.example.moviemvvmapp.data.api.TheMovieDBInterface
import com.example.moviemvvmapp.data.repository.NetworkState
import com.example.moviemvvmapp.data.vo.MovieDetails
import kotlinx.android.synthetic.main.activity_single_movie.*
import java.text.NumberFormat
import java.util.*

class SingleMovie : AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

        val movieId: Int = intent.getIntExtra("id",1)

        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            text_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE

        })

    }

    private fun bindUI(it: MovieDetails) {
        movie_title.text = it.title
        movie_runtime.text = "${it.runtime} mins"
        val formatCurrency: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)
        movie_revenue.text = formatCurrency.format(it.revenue)
        val moviePosterURL: String = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(iv_movie_poster)
    }

    private fun getViewModel(movieId:Int): SingleMovieViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieRepository,movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }

}
