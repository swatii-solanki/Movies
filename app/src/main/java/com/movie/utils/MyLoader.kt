package com.movie.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.movie.R
import com.movie.databinding.ItemLoaderBinding

class MyLoader(context: Context){

    private lateinit var dialog: Dialog

    init {
        val inflater = LayoutInflater.from(context)
        val binding: ItemLoaderBinding = DataBindingUtil.inflate(inflater, R.layout.item_loader, null, false)
        try {
            dialog = Dialog(context)
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(binding.root)
            dialog.setCancelable(false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        if (dialog.isShowing) dialog.dismiss()
    }
}