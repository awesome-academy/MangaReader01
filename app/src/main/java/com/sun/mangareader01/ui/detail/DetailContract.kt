package com.sun.mangareader01.ui.detail

import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.model.MangaDetail

interface DetailContract {
    interface View {
        fun showMangaDetail(data: MangaDetail)
        fun showError(exception: Exception)
    }

    interface Presenter {
        fun getDetail(manga: Manga)
    }
}
