package com.example.moviemvvmapp.single_movie_details

import androidx.lifecycle.LiveData
import com.example.moviemvvmapp.data.api.TheMovieDBInterface
import com.example.moviemvvmapp.data.repository.MovieDetailsNetworkDataSource
import com.example.moviemvvmapp.data.repository.NetworkState
import com.example.moviemvvmapp.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository (private val apiService: TheMovieDBInterface) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails(compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails> {
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieDetailsResponse
    }

    fun getMovieDetailsNetworkState() : LiveData<NetworkState> = movieDetailsNetworkDataSource.networkState
}