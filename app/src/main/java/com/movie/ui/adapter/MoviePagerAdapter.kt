package com.movie.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.movie.R
import com.movie.data.response.MMovie
import com.movie.databinding.ItemPagerMovieBinding

class MoviePagerAdapter(private val context: Context) : RecyclerView.Adapter<MoviePagerAdapter.ViewHolder>() {

    private lateinit var binding: ItemPagerMovieBinding
    private var movies: ArrayList<MMovie> = ArrayList()

    class ViewHolder(val binding: ItemPagerMovieBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_pager_movie,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/original${movie.backdrop_path}")
            .thumbnail(0.1f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.iv)
    }

    override fun getItemCount() = movies.size

    fun setMovies(movies: ArrayList<MMovie>) {
        this.movies = movies
        notifyDataSetChanged()
    }
}