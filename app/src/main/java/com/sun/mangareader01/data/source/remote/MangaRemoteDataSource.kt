package com.sun.mangareader01.data.source.remote

import com.sun.mangareader01.data.model.DataRequest
import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.model.MangaDetailResponse
import com.sun.mangareader01.data.model.MangasResponse
import com.sun.mangareader01.data.source.MangaDataSource
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback
import com.sun.mangareader01.utils.Constants.AUTHORITY_READ_COMICS_ONLINE
import com.sun.mangareader01.utils.Constants.SCHEME_HTTPS
import com.sun.mangareader01.utils.PathConstants
import com.sun.mangareader01.utils.PathConstants.PATH_COMIC

class MangaRemoteDataSource : MangaDataSource.Remote {

    override fun getMangas(
        query: String,
        callback: OnLoadedDataCallback<MangasResponse>
    ) {
        val requestUrl = DataRequest(
            scheme = SCHEME_HTTPS,
            authority = AUTHORITY_READ_COMICS_ONLINE,
            paths = listOf(PathConstants.PATH_SEARCH),
            queryParams = mapOf(KEY_QUERY to query)
        ).toUrl()
        GetResponseAsync(MangasResponseHandler(), callback).execute(requestUrl)
    }

    override fun getMangaDetail(
        manga: Manga,
        callback: OnLoadedDataCallback<MangaDetailResponse>
    ) {
        val requestUrl = DataRequest(
            scheme = SCHEME_HTTPS,
            authority = AUTHORITY_READ_COMICS_ONLINE,
            paths = listOf(PATH_COMIC, manga.slug)
        ).toUrl()
        GetResponseAsync(MangaDetailResponseHandler(), callback).execute(requestUrl)
    }

    companion object {
        const val KEY_QUERY = "query"
    }
}
