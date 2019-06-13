package com.sun.mangareader01.ui.search

import com.sun.mangareader01.data.model.Manga

interface SearchContract {
    interface View {
        fun showMangas(mangas: List<Manga>)
        fun showError(exception: Exception)
    }

    interface Presenter {
        fun getMangas(query: String)
    }
}
