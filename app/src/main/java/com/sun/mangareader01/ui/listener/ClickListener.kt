package com.sun.mangareader01.ui.listener

interface ClickListener {
    fun <T> onClick(item: T): Unit?
}
