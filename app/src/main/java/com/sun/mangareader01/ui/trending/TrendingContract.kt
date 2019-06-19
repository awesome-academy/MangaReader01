package com.sun.mangareader01.ui.trending

import com.sun.mangareader01.data.model.Manga

interface TrendingContract {
    interface View {
        fun showMostViewed(mangas: List<Manga>)
        fun showLastRelease(mangas: List<Manga>)
        fun showHotMangas(mangas: List<Manga>)
        fun showError(exception: Exception)
    }

    interface Presenter {
        fun getTrending()
        fun getMostViewedMangas()
        fun getHotMangas()
        fun getLastReleaseMangas()
    }
}
