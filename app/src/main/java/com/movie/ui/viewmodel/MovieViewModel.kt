package com.movie.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.data.network.Resource
import com.movie.data.repo.MovieRepo
import com.movie.data.response.MMovieResponse
import kotlinx.coroutines.launch

class MovieViewModel(private val repo: MovieRepo, private val page: Int) : ViewModel() {

    private val _movieResponse: MutableLiveData<Resource<MMovieResponse>> = MutableLiveData()

    val movieResponse: LiveData<Resource<MMovieResponse>>
        get() = _movieResponse

    fun getMovies() = viewModelScope.launch {
        _movieResponse.value = Resource.Loading
        _movieResponse.value = repo.getMovie(page)
    }

}