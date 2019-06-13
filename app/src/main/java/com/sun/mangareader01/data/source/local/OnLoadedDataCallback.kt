package com.sun.mangareader01.data.source.local

interface OnLoadedDataCallback<T> {
    fun onSuccessful(data: T)
    fun onFailed(exception: Exception)
}
