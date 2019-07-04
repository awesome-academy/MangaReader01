package com.sun.mangareader01.ui.listener

import android.os.Parcelable
import com.sun.mangareader01.data.model.Chapter
import com.sun.mangareader01.data.model.Manga

interface OnItemClickListener : Parcelable {
    fun onMangaClick(manga: Manga?)
    fun onChapterClick(chapter: Chapter?)
    fun onTagClick(tag: String?)

    fun onDeleteManga(manga: Manga?)
    fun onDownloadManga(manga: Manga?)
}
