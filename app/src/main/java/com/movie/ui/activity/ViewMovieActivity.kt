package com.movie.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.movie.R
import com.movie.data.network.API
import com.movie.data.network.Resource
import com.movie.data.network.RetrofitClient
import com.movie.data.repo.MovieRepo
import com.movie.data.response.MMovie
import com.movie.databinding.ActivityViewMovieBinding
import com.movie.ui.adapter.GenreAdapter
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
        binding.toolbarTitle.text = getString(R.string.movies)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        val id = intent.getIntExtra("id", -1)
        loader = MyLoader(this)
        initializeViewModel()
        if (Utility.isNetworkAvailable(this)) {
            viewModel.getMovieDetail(id)
        } else {
            binding.tvNoInternet.visibility = View.VISIBLE
        }
        viewModel.movieDetailResponse.observe(this, {
            when (it) {
                is Resource.Loading -> loader.show()
                is Resource.Success -> {
                    loader.dismiss()
                    binding.tvNoInternet.visibility = View.GONE
                    binding.root.visibility = View.VISIBLE
                    Log.d(TAG, "init: $it")
                    setData(it.value)
                }
                is Resource.Failure -> {
                    binding.tvNoInternet.visibility = View.VISIBLE
                    binding.tvNoInternet.text = getString(R.string.something_went_wrong)
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

    private fun setData(movie: MMovie) {
        binding.apply {

            Glide.with(this@ViewMovieActivity)
                .load("https://image.tmdb.org/t/p/w400${movie.poster_path}")
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.iv)

            tvMovieName.text = movie.original_title
            tvDate.text = movie.release_date
            tvReview.text = "Reviews : ${movie.vote_count} (User)"
            ratingBar.rating = movie.vote_average / 2
            tvRatingCount.text = "${movie.vote_average}"
            tvDesc.text = movie.overview

            val layoutManager =
                LinearLayoutManager(this@ViewMovieActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.rvGenre.layoutManager = layoutManager
            binding.rvGenre.setHasFixedSize(true)
            val adapter = GenreAdapter(movie.genres)
            binding.rvGenre.adapter = adapter
        }
    }

    private fun initializeViewModel() {
        val repo = MovieRepo(RetrofitClient.buildApi(API::class.java))
        viewModel = ViewModelProvider(
            this,
            MovieViewModelFactory(repo)
        ).get(MovieViewModel::class.java)
    }

}