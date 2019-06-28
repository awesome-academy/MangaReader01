package com.sun.mangareader01.data.source.repository

import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.model.MangaDetail
import com.sun.mangareader01.data.model.MangasResponse
import com.sun.mangareader01.data.source.MangaDataSource
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback

object MangaRepository : MangaDataSource.Remote {

    private var remote: MangaDataSource.Remote? = null

    fun initDataSource(remoteDataSource: MangaDataSource.Remote) {
        remote = remoteDataSource
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

    override fun getHotMangas(callback: OnLoadedDataCallback<MangasResponse>) {
        remote?.getHotMangas(callback)
    }

    override fun getLastReleasedMangas(callback: OnLoadedDataCallback<MangasResponse>) {
        remote?.getLastReleasedMangas(callback)
    }

    override fun getMostViewedMangas(callback: OnLoadedDataCallback<MangasResponse>) {
        remote?.getMostViewedMangas(callback)
    }
}
