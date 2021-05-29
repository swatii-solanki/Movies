package com.movie.utils

import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.movie.R
import kotlin.math.roundToInt

object Utility {

    private lateinit var snackBar: Snackbar

    // for show snackbar
    fun showSnackBar(mContext: Context, v: View?, msg: String?) {
        snackBar = Snackbar.make(v!!, msg!!, Snackbar.LENGTH_LONG)
            .setAction(mContext.getString(R.string.ok)) { snackBar.dismiss() }
        val params = snackBar.view.layoutParams as MarginLayoutParams
        snackBar.view.layoutParams = params
        val message: TextView = snackBar.view.findViewById(R.id.snackbar_text)
        message.maxLines = 3
        snackBar.show()
    }


    // checking whether network is available or not
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
        return false
    }

    fun Int.toPx(): Int {
        val metrics = Resources.getSystem().displayMetrics
        val px = this * (metrics.densityDpi / 160f)
        return px.roundToInt()
    }
}