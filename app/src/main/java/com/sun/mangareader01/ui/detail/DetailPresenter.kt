package com.sun.mangareader01.ui.detail

import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.model.MangaDetail
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback
import com.sun.mangareader01.data.source.remote.MangaRemoteDataSource
import com.sun.mangareader01.data.source.repository.MangaRepository

class DetailPresenter(
    private val view: DetailContract.View,
    private val repository: MangaRepository
) : DetailContract.Presenter {

    init {
        repository.initDataSource(MangaRemoteDataSource())
    }

    override fun getDetail(manga: Manga) {
        repository.getMangaDetail(manga, object : OnLoadedDataCallback<MangaDetail> {

            override fun onSuccessful(data: MangaDetail) = view.showMangaDetail(data)

            override fun onFailed(exception: Exception) =
                view.showError(exception)

        })
    }
}