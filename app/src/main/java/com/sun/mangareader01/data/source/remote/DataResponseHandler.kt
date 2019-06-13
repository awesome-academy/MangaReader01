package com.sun.mangareader01.data.source.remote

import com.sun.mangareader01.utils.Constants.METHOD_GET
import com.sun.mangareader01.utils.Extensions.build
import com.sun.mangareader01.utils.Extensions.getString
import org.json.JSONException
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

interface DataResponseHandler<T> {

    @Throws(JSONException::class)
    fun parseToObject(string: String): T

    @Throws(IOException::class, JSONException::class)
    fun getResponse(url: String): T? {
        var responseData: T?
        var connection: HttpURLConnection? = null
        try {
            connection = (URL(url).openConnection() as HttpURLConnection).apply {
                build(METHOD_GET)
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw IOException(responseCode.toString())
                }
                InputStreamReader(inputStream).run {
                    responseData = parseToObject(getString())
                    close()
                }
            }
        } finally {
            connection?.disconnect()
        }
        return responseData
    }
}
