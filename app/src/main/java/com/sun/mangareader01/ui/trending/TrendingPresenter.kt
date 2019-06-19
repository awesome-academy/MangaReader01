package com.sun.mangareader01.ui.trending

import com.sun.mangareader01.data.source.repository.MangaRepository

class TrendingPresenter(
    private val view: TrendingContract.View,
    private val repository: MangaRepository
) : TrendingContract.Presenter {

    override fun getTrending() {
    }

    override fun getMostViewedMangas() {
    }

    override fun getHotMangas() {
    }

    override fun getLastReleaseMangas() {
    }
}
