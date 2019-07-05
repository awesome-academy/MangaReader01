package com.sun.mangareader01.data.source.repository

import com.sun.mangareader01.data.model.*
import com.sun.mangareader01.data.source.MangaDataSource
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback

object MangaRepository : MangaDataSource.Remote, MangaDataSource.Local {

    private var remote: MangaDataSource.Remote? = null
    private var local: MangaDataSource.Local? = null

    fun initDataSource(
        remoteDataSource: MangaDataSource.Remote,
        localDataSource: MangaDataSource.Local? = null
    ) {
        remote = remoteDataSource
        local = localDataSource
    }

    override fun getMangas(
        query: String,
        callback: OnLoadedDataCallback<MangasResponse>
    ) {
        remote?.getMangas(query, callback)
    }

    override fun getMangaDetail(
        manga: Manga,
        callback: OnLoadedDataCallback<MangaDetail>
    ) {
        remote?.getMangaDetail(manga, callback)
    }

    override fun getFilteredMangas(
        page: Int,
        category: String,
        alpha: String,
        sortBy: String,
        asc: Boolean,
        author: String,
        tag: String,
        callback: OnLoadedDataCallback<MangasResponse>
    ) {
        remote?.getFilteredMangas(
            page = page,
            category = category,
            alpha = alpha,
            sortBy = sortBy,
            asc = asc,
            author = author,
            tag = tag,
            callback = callback
        )
    }

    override fun getRandomMangaDetail(callback: OnLoadedDataCallback<MangaDetail>) {
        remote?.getRandomMangaDetail(callback)
    }

    override fun getHotMangas(callback: OnLoadedDataCallback<MangasResponse>) {
        remote?.getHotMangas(callback)
    }

    override fun getLastReleasedMangas(callback: OnLoadedDataCallback<MangasResponse>) {
        remote?.getLastReleasedMangas(callback)
    }

    override fun getMostViewedMangas(callback: OnLoadedDataCallback<MangasResponse>) {
        remote?.getMostViewedMangas(callback)
    }

    override fun getPages(
        chapter: Chapter,
        callback: OnLoadedDataCallback<PagesResponse>
    ) {
        remote?.getPages(chapter, callback)
    }

    override fun getCategories(callback: OnLoadedDataCallback<CategoriesResponse>) {
        remote?.getCategories(callback)
    }

    override fun getMyMangas(callback: OnLoadedDataCallback<MangasResponse>) {
        local?.getMyMangas(callback)
    }

    override fun insertMangas(
        manga: Manga,
        callback: OnLoadedDataCallback<Boolean>
    ) {
        local?.insertMangas(manga, callback)
    }

    override fun deleteManga(
        manga: Manga,
        callback: OnLoadedDataCallback<Boolean>
    ) {
        local?.deleteManga(manga, callback)
    }
}
