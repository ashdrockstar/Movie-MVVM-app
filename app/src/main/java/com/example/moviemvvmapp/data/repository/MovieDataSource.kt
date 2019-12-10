package com.example.moviemvvmapp.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.moviemvvmapp.data.api.FIRST_PAGE
import com.example.moviemvvmapp.data.api.TheMovieDBInterface
import com.example.moviemvvmapp.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource(private val apiService : TheMovieDBInterface, private val compositeDesposable: CompositeDisposable) : PageKeyedDataSource<Int, Movie>(){

    private var page = FIRST_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositeDesposable.add(
            apiService.getPopularMovies(page)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    callback.onResult(it.moviesList, null, page+1)
                    networkState.postValue(NetworkState.LOADED)
                },{
                    networkState.postValue(NetworkState.ERROR)
                    Log.e("MovieDataSource",it.message)
                })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDesposable.add(
            apiService.getPopularMovies(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if(it.totalPages >= params.key) {
                        callback.onResult(it.moviesList, params.key+1)
                        networkState.postValue(NetworkState.LOADED)
                    }
                    else{
                        networkState.postValue(NetworkState.ENDOFLIST)
                    }

                },{
                    networkState.postValue(NetworkState.ERROR)
                    Log.e("MovieDataSource",it.message)

                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}