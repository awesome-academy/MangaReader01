package com.sun.mangareader01.data.source.repository

import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.model.MangaDetailResponse
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
        callback: OnLoadedDataCallback<MangaDetailResponse>
    ) {
        remote?.getMangaDetail(manga, callback)
    }
}
