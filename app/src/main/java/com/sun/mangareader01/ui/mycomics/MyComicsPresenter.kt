package com.sun.mangareader01.ui.mycomics

import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.model.MangaDetail
import com.sun.mangareader01.data.model.MangasResponse
import com.sun.mangareader01.data.model.PagesResponse
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
            override fun onSuccessful(data: Boolean) {
                view.deleteMangaFiles(manga)
            }

            override fun onFailed(exception: Exception) =
                view.showError(exception)
        })

    override fun saveManga(manga: Manga) = repository.getMangaDetail(
        manga,
        object : OnLoadedDataCallback<MangaDetail> {
            override fun onSuccessful(data: MangaDetail) {
                view.showNoticeLoadPageUrls()
                getPageUrlsThenDownload(data)
            }

            override fun onFailed(exception: Exception) =
                view.showError(exception)
        })

    override fun getPageUrlsThenDownload(mangaDetail: MangaDetail) {
        for (chapter in mangaDetail.chapters) {
            repository.getPages(
                chapter,
                object : OnLoadedDataCallback<PagesResponse> {
                    override fun onSuccessful(data: PagesResponse) {
                        chapter.pageUrls.clear()
                        chapter.pageUrls.addAll(data.pageUrls)
                        if (chapter == mangaDetail.chapters.last()) {
                            view.downloadMangaFiles(mangaDetail)
                        }
                    }

                    override fun onFailed(exception: Exception) =
                        view.showError(exception)
                })
        }
    }

    override fun updateManga(manga: Manga) = repository.updateManga(
        manga,
        object : OnLoadedDataCallback<Boolean> {
            override fun onSuccessful(data: Boolean) {
            }

            override fun onFailed(exception: Exception) =
                view.showError(exception)
        }
    )
}
