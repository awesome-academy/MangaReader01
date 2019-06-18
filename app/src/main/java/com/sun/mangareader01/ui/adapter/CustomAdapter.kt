package com.sun.mangareader01.ui.adapter

interface CustomAdapter<E> {

    var onItemClickListener: OnItemClickListener<E>?

    fun updateData(data: List<E>)
    fun <T> updateValue(value: T)

    interface OnItemClickListener<E> {
        fun onItemClick(item: E)
    }
}
