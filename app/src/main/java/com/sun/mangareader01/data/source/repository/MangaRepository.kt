package com.sun.mangareader01.data.source.repository

import com.sun.mangareader01.data.model.MangasResponse
import com.sun.mangareader01.data.source.MangaDataSource
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback
import com.sun.mangareader01.data.source.remote.MangaRemoteDataSource

object MangaRepository : MangaDataSource.Remote {

    private lateinit var dataSource: MangaRemoteDataSource

    fun fromDataSource(mangaRemoteDataSource: MangaRemoteDataSource) =
        apply { dataSource = mangaRemoteDataSource }

    override fun getMangas(query: String, callback: OnLoadedDataCallback<MangasResponse>) {
        dataSource.getMangas(query, callback)
    }
}
