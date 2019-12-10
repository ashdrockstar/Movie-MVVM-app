package com.example.moviemvvmapp.data.api

import com.example.moviemvvmapp.data.vo.MovieDetails
import com.example.moviemvvmapp.data.vo.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBInterface {
//    https://api.themoviedb.org/3/movie/330457?api_key=f4908b2680469540964d6e2cac688779&language=en-US

//    https://api.themoviedb.org/3/

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Single<MovieResponse>
}