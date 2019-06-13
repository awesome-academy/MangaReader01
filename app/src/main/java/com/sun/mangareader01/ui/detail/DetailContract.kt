package com.sun.mangareader01.ui.detail

import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.model.MangaDetailResponse

interface DetailContract {
    interface View {
        fun showDetail(data: MangaDetailResponse)
        fun showError(exception: Exception)
    }

    interface Presenter {
        fun getDetail(manga: Manga)
    }
}
