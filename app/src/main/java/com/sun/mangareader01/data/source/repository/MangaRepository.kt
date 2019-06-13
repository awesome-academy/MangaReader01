package com.sun.mangareader01.data.source.repository

import com.sun.mangareader01.data.model.MangasResponse
import com.sun.mangareader01.data.source.MangaDataSource
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback

object MangaRepository : MangaDataSource.Remote {

    private var remote: MangaDataSource.Remote? = null
    private var local: MangaDataSource.Local? = null

    fun initDataSource(
        remoteDataSource: MangaDataSource.Remote,
        localDataSource: MangaDataSource.Local
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
}
