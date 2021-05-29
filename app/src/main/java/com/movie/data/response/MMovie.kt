package com.movie.data.response

data class MMovie(
        val adult: Boolean,
        val backdrop_path: String,
        val budget: Long,
        val genres: ArrayList<Genre>,
        val id: Int,
        val original_title: String,
        val overview: String,
        val popularity: Float,
        val poster_path: String,
        val release_date: String,
        val status: String,
        val tagline: String,
        val title: String,
        val vote_average: Float,
        val vote_count: Long,
)


data class Genre(
        val id: Int,
        val name: String
)
