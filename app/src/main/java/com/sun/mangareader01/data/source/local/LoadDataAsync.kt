package com.sun.mangareader01.data.source.local

import android.os.AsyncTask

class LoadDataAsync<T>(private val callback: OnLoadedDataCallback<T>) :
    AsyncTask<T, Void, T?>() {

    private var exception: Exception? = null

    override fun doInBackground(vararg params: T): T? =
        try {
            params.first()
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
