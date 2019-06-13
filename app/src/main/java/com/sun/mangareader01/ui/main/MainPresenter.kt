package com.sun.mangareader01.ui.main

import com.sun.mangareader01.data.model.MangasResponse
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback
import com.sun.mangareader01.data.source.remote.MangaRemoteDataSource
import com.sun.mangareader01.data.source.repository.MangaRepository

const val SUGGESTIONS_LIMIT = 5

class MainPresenter(
    private val view: MainContract.View
) : MainContract.Presenter {

    init {
        view.setPresenter(this)
        MangaRepository.initDataSource(MangaRemoteDataSource())
    }

    override fun getSuggestions(query: String) =
        MangaRepository.getMangas(query, object : OnLoadedDataCallback<MangasResponse> {
            override fun onSuccessful(data: MangasResponse) =
                view.showSuggestions(data.mangas.take(SUGGESTIONS_LIMIT))

            override fun onFailed(exception: Exception) = view.showError(exception)
        })
}
