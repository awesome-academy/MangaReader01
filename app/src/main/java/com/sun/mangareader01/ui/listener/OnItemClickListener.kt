package com.sun.mangareader01.ui.listener

import com.sun.mangareader01.data.model.Chapter
import com.sun.mangareader01.data.model.Manga

interface OnItemClickListener {
    fun onMangaClick(manga: Manga?)
    fun onChapterClick(chapter: Chapter?)
    fun onTagClick(tag: String?)
}
