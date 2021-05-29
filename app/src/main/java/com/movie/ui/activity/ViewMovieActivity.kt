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
import com.movie.databinding.ActivityViewMovieBinding
import com.movie.ui.viewmodel.MovieViewModel
import com.movie.ui.viewmodel.MovieViewModelFactory
import com.movie.utils.MyLoader
import com.movie.utils.Utility

class ViewMovieActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ViewMovieActivity"
    }

    private lateinit var binding: ActivityViewMovieBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var loader: MyLoader


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_movie)
        init()
    }

    private fun init() {
        val id = intent.getIntExtra("id", -1)
        loader = MyLoader(this)
        initializeViewModel()
        viewModel.getMovieDetail(id)
        viewModel.movieDetailResponse.observe(this, {
            when (it) {
                is Resource.Loading -> loader.show()
                is Resource.Success -> {
                    loader.dismiss()
                    Log.d(TAG, "init: $it")
                }
                is Resource.Failure -> {
                    loader.dismiss()
                    Utility.showSnackBar(
                        this,
                        binding.root,
                        getString(R.string.something_went_wrong)
                    )
                }
            }
        })
    }

    private fun initializeViewModel() {
        val repo = MovieRepo(RetrofitClient.buildApi(API::class.java))
        viewModel = ViewModelProvider(
            this,
            MovieViewModelFactory(repo)
        ).get(MovieViewModel::class.java)
    }

}