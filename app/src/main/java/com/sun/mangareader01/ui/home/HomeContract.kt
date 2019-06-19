package com.sun.mangareader01.ui.home

import com.sun.mangareader01.data.model.Category
import com.sun.mangareader01.data.model.MangaDetail

interface HomeContract {
    interface View {
        fun showRandomMangaDetail(mangaDetail: MangaDetail)
        fun showCategories(categories: List<Category>)
        fun showError(exception: Exception)
        fun updateMangasCategory(position: Int, category: Category)
    }

    interface Presenter {
        fun getRandomMangaDetail()
        fun getCategories()
        fun getMangas(position: Int, category: Category)
    }
}
