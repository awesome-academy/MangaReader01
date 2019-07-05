package com.sun.mangareader01.ui.mycomics

import com.sun.mangareader01.data.model.Manga

interface MyComicsContract {

    interface View {
        fun showMangas(mangas: List<Manga>)
        fun showError(exception: Exception)
        fun confirmDeleted(successful: Boolean)
    }

    interface Presenter {
        fun getMyMangas()
        fun deleteManga(manga: Manga)
    }
}
