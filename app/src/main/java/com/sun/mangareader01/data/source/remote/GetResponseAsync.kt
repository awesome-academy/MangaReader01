package com.sun.mangareader01.data.source.remote

import android.os.AsyncTask
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback

class GetResponseAsync<T>(
    private val responseHandler: DataResponseHandler<T>,
    private val callback: OnLoadedDataCallback<T>
) : AsyncTask<String, Void, T?>() {

    private var exception: Exception? = null

    override fun doInBackground(vararg params: String): T? =
        try {
            responseHandler.getResponse(params[0])
        } catch (exception: Exception) {
            this.exception = exception
            null
        }

    override fun onPostExecute(result: T?) {
        result?.let {
            callback.onSuccessful(it)
        } ?: exception?.let {
            callback.onFailed(it)
        }
    }
}
