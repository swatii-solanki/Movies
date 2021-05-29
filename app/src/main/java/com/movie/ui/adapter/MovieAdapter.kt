package com.movie.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.movie.R
import com.movie.data.response.MMovie
import com.movie.databinding.ItemMovieBinding
import com.movie.ui.activity.ViewMovieActivity

class MovieAdapter(private val context: Context) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private lateinit var binding: ItemMovieBinding
    private var movies: ArrayList<MMovie> = ArrayList()

    class ViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_movie,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/w400${movie.poster_path}")
            .thumbnail(0.1f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.iv)

        holder.itemView.setOnClickListener {
            val intent = Intent(this.context,ViewMovieActivity::class.java)
            intent.putExtra("id",movie.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = movies.size

    fun setMovies(movies: ArrayList<MMovie>) {
        this.movies = movies
        notifyDataSetChanged()
    }
}