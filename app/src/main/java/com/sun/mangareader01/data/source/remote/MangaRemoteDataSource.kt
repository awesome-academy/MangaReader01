package com.sun.mangareader01.data.source.remote

import com.sun.mangareader01.data.model.DataRequest
import com.sun.mangareader01.data.model.MangasResponse
import com.sun.mangareader01.data.source.MangaDataSource
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback
import com.sun.mangareader01.utils.AUTHORITY_READ_COMICS_ONLINE
import com.sun.mangareader01.utils.PathConstants.Companion.PATH_SEARCH
import com.sun.mangareader01.utils.SCHEME_HTTPS

object MangaRemoteDataSource : MangaDataSource.Remote {
    override fun getMangas(query: String, callback: OnLoadedDataCallback<MangasResponse>) {
        val requestUrl = DataRequest(
            scheme = SCHEME_HTTPS,
            authority = AUTHORITY_READ_COMICS_ONLINE,
            paths = listOf(PATH_SEARCH)
        ).toUrl()
        GetResponsesAsync(MangasResponseHandler(), callback).execute(requestUrl)
    }
}
