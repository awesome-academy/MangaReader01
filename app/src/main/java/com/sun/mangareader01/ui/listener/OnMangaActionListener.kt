package com.sun.mangareader01.ui.listener

import com.sun.mangareader01.data.model.Manga

interface OnMangaActionListener {
    fun onDeleteManga(manga: Manga?)
    fun onDownloadManga(manga: Manga?)
}
