package com.sun.mangareader01.ui.adapter

import com.sun.mangareader01.ui.listener.ClickListener

interface CustomAdapter<E> {
    var onItemClickListener: ClickListener?
    fun updateData(data: List<E>)
    fun <T> updateValue(value: T)
}
