package com.sun.mangareader01.data.source.local

interface SavePagesCallback {
    fun onComplete(isSuccessful: Boolean)
    fun onUpdate(done: Int)
}
