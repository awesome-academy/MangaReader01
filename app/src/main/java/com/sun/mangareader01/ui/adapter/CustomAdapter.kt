package com.sun.mangareader01.ui.adapter

import com.sun.mangareader01.ui.listener.OnItemClickListener

interface CustomAdapter<E> {
    var onItemClickListener: OnItemClickListener?
    fun updateData(data: List<E>)
    fun <T> updateValue(value: T)
}
