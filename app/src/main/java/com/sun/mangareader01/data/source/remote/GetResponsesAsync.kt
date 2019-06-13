package com.sun.mangareader01.data.source.remote

import android.os.AsyncTask
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback

class GetResponsesAsync<T>(
    private val responseHandler: DataResponseHandler<T>,
    private val callback: OnLoadedDataCallback<T>
) : AsyncTask<String, Void, Exception?>() {

    private var result: T? = null

    override fun doInBackground(vararg params: String) =
        try {
            result = responseHandler.getResponse(params[0])
            null
        } catch (exception: Exception) {
            exception
        }

    override fun onPostExecute(exception: Exception?) {
        exception?.let {
            callback.onFailed(exception)
        } ?: result?.let {
            callback.onSuccessful(it)
        }
    }
}
