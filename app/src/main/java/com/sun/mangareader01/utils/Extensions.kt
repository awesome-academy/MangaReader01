package com.sun.mangareader01.utils

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.sun.mangareader01.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection

object Extensions {
    fun Context.showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    @Throws(IOException::class)
    fun HttpURLConnection.build(method: String) = apply {
        requestMethod = method
        connect()
    }

    @Throws(IOException::class)
    fun InputStreamReader.getString(): String {
        val reader = BufferedReader(this)
        return StringBuilder().apply {
            do {
                val inputLine = reader.readLine()
                inputLine?.let { append(inputLine) }
            } while (inputLine != null)
            reader.close()
        }.toString()
    }

    fun ImageView.setImageUrl(
        url: String,
        circleCrop: Boolean = false,
        defaultResourceId: Int = R.color.color_light_gray
    ) {
        Glide.with(context)
            .load(url)
            .apply { if (circleCrop) circleCrop() }
            .placeholder(defaultResourceId)
            .into(this)
    }
}
