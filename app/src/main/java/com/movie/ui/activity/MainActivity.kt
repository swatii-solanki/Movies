package com.movie.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.movie.R
import com.movie.data.network.API
import com.movie.data.network.Resource
import com.movie.data.network.RetrofitClient
import com.movie.data.repo.MovieRepo
import com.movie.databinding.ActivityMainBinding
import com.movie.ui.viewmodel.MovieViewModel
import com.movie.ui.viewmodel.MovieViewModelFactory

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        const val page = 1
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    private fun init() {
        initializeViewModel()

        viewModel.movieResponse.observe(this, {
            if (it is Resource.Success) {
                Log.d(TAG, "init: ${it.toString()}")
            }
        })

        viewModel.getMovies()
    }

    private fun initializeViewModel() {
        val repo = MovieRepo(RetrofitClient.buildApi(API::class.java))
        viewModel = ViewModelProvider(
            this,
            MovieViewModelFactory(repo, page)
        ).get(MovieViewModel::class.java)
    }

}