package com.movie.data.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MMovie(
        val backdrop_path: String,
        val id: Int,
        val original_title: String,
        val overview: String,
        val poster_path: String
) : Parcelable
