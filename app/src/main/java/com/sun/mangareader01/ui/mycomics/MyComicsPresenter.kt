package com.sun.mangareader01.ui.mycomics

import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.model.MangasResponse
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback
import com.sun.mangareader01.data.source.repository.MangaRepository

class MyComicsPresenter(
    private val view: MyComicsContract.View,
    private val repository: MangaRepository
) : MyComicsContract.Presenter {

    override fun getMyMangas() = repository.getMyMangas(
        object : OnLoadedDataCallback<MangasResponse> {
            override fun onSuccessful(data: MangasResponse) =
                view.showMangas(data.mangas)

            override fun onFailed(exception: Exception) =
                view.showError(exception)
        }
    )

    override fun deleteManga(manga: Manga) = repository.deleteManga(
        manga,
        object : OnLoadedDataCallback<Boolean> {
            override fun onSuccessful(data: Boolean) = view.confirmDeleted(data)

            override fun onFailed(exception: Exception) =
                view.showError(exception)
        }
    )
}
