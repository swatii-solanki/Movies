package com.movie.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.movie.R
import com.movie.data.response.Genre
import com.movie.databinding.ItemGenreBinding

class GenreAdapter(private var genre: ArrayList<Genre>) :
    RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    private lateinit var binding: ItemGenreBinding

    class ViewHolder(val binding: ItemGenreBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_genre,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = genre[position]
        holder.binding.tvGenre.text = genre.name
    }

    override fun getItemCount() = genre.size
}