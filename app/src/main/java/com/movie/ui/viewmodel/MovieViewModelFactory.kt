package com.movie.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.movie.data.repo.MovieRepo

class MovieViewModelFactory(private val repository: MovieRepo, private val page: Int) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            return MovieViewModel(repository, page) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
