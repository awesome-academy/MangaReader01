package com.sun.mangareader01.ui.read

import com.sun.mangareader01.data.model.Chapter
import com.sun.mangareader01.data.model.PagesResponse
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback
import com.sun.mangareader01.data.source.repository.MangaRepository

class ReadPresenter(
    private val view: ReadContract.View,
    private val repository: MangaRepository
) : ReadContract.Presenter {

    override fun getPages(chapter: Chapter) = repository.getPages(
        chapter,
        object : OnLoadedDataCallback<PagesResponse> {
            override fun onSuccessful(data: PagesResponse) =
                view.showPages(data.pageUrls)

            override fun onFailed(exception: Exception) =
                view.showError(exception)
        })
}
