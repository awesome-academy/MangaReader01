package com.sun.mangareader01.ui.trending.pager

import com.sun.mangareader01.data.model.Manga

interface PagerContract {
    interface View {
        fun showMangas(mangas: List<Manga>)
        fun showError(exception: Exception)
    }

    interface Presenter {
        fun getMangas()
    }
}
