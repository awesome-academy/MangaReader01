package com.sun.mangareader01.ui.trending.mostviewed

import com.sun.mangareader01.data.model.MangasResponse
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback
import com.sun.mangareader01.data.source.repository.MangaRepository
import com.sun.mangareader01.ui.trending.pager.PagerContract

class MostViewedPresenter(
    private val view: PagerContract.View,
    private val repository: MangaRepository
) : PagerContract.Presenter {

    override fun getMangas() = repository.getMostViewedMangas(
        object : OnLoadedDataCallback<MangasResponse> {
            override fun onSuccessful(data: MangasResponse) =
                view.showMangas(data.mangas)

            override fun onFailed(exception: Exception) =
                view.showError(exception)
        }
    )
}
