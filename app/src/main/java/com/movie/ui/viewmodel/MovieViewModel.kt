package com.movie.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.data.network.Resource
import com.movie.data.repo.MovieRepo
import com.movie.data.response.MMovie
import com.movie.data.response.MMovieResponse
import kotlinx.coroutines.launch

class MovieViewModel(private val repo: MovieRepo) : ViewModel() {

    private val _movieResponse: MutableLiveData<Resource<MMovieResponse>> = MutableLiveData()
    private val _movieDetailResponse: MutableLiveData<Resource<MMovie>> = MutableLiveData()

    val movieResponse: LiveData<Resource<MMovieResponse>>
        get() = _movieResponse

    val movieDetailResponse: LiveData<Resource<MMovie>>
        get() = _movieDetailResponse

    fun getMovies(page: Int) = viewModelScope.launch {
        _movieResponse.value = Resource.Loading
        _movieResponse.value = repo.getMovie(page)
    }

    fun getMovieDetail(id: Int) = viewModelScope.launch {
        _movieDetailResponse.value = Resource.Loading
        _movieDetailResponse.value = repo.getMovieDetail(id)
    }

}