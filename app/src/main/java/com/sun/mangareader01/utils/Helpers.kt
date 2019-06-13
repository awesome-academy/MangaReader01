package com.sun.mangareader01.utils

import android.content.Context
import android.widget.Toast
import com.sun.mangareader01.data.model.DataRequest
import com.sun.mangareader01.utils.PathConstants.Companion.PATH_COVER
import com.sun.mangareader01.utils.PathConstants.Companion.PATH_COVER_FILE_NAME
import com.sun.mangareader01.utils.PathConstants.Companion.PATH_MANGA
import com.sun.mangareader01.utils.PathConstants.Companion.PATH_UPLOADS
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection

fun buildCoverUrl(slug: String) = DataRequest(
    scheme = SCHEME_HTTPS,
    authority = AUTHORITY_READ_COMICS_ONLINE,
    paths = listOf(
        PATH_UPLOADS, PATH_MANGA, slug,
        PATH_COVER, PATH_COVER_FILE_NAME
    )
).toUrl()

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

@Throws(IOException::class)
fun HttpURLConnection.build(method: String) = apply {
    requestMethod = method
    connect()
}

@Throws(IOException::class)
fun InputStreamReader.getJsonString(): String {
    val reader = BufferedReader(this)
    return StringBuilder().apply {
        do {
            val inputLine = reader.readLine()
            inputLine?.let { append(inputLine) }
        } while (inputLine != null)
        reader.close()
    }.toString()
}
