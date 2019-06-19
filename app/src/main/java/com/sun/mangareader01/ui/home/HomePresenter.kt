package com.sun.mangareader01.ui.home

import com.sun.mangareader01.data.model.CategoriesResponse
import com.sun.mangareader01.data.model.Category
import com.sun.mangareader01.data.model.MangaDetail
import com.sun.mangareader01.data.model.MangasResponse
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback
import com.sun.mangareader01.data.source.repository.MangaRepository

class HomePresenter(
    private val view: HomeContract.View,
    private val repository: MangaRepository
) : HomeContract.Presenter {

    override fun getRandomMangaDetail() = repository.getRandomMangaDetail(
        object : OnLoadedDataCallback<MangaDetail> {
            override fun onSuccessful(data: MangaDetail) =
                view.showRandomMangaDetail(data)

            override fun onFailed(exception: Exception) =
                view.showError(exception)
        }
    )

    override fun getCategories() = repository.getCategories(
        callback = object : OnLoadedDataCallback<CategoriesResponse> {
            override fun onSuccessful(data: CategoriesResponse) =
                view.showCategories(data.categories)

            override fun onFailed(exception: Exception) =
                view.showError(exception)
        })

    override fun getMangas(position: Int, category: Category) =
        repository.getFilteredMangas(
            category = category.id.toString(),
            callback = object : OnLoadedDataCallback<MangasResponse> {
                override fun onSuccessful(data: MangasResponse) {
                    category.mangas.clear()
                    category.mangas.addAll(data.mangas)
                    view.updateMangasCategory(position, category)
                }

                override fun onFailed(exception: Exception) =
                    view.showError(exception)
            })
}
