package com.sun.mangareader01.data.source.remote

import com.sun.mangareader01.data.model.DataRequest
import com.sun.mangareader01.data.model.MangasResponse
import com.sun.mangareader01.data.source.MangaDataSource
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback
import com.sun.mangareader01.utils.Constants
import com.sun.mangareader01.utils.PathConstants

class MangaRemoteDataSource : MangaDataSource.Remote {

    override fun getMangas(
        query: String,
        callback: OnLoadedDataCallback<MangasResponse>
    ) {
        val requestUrl = DataRequest(
            scheme = Constants.SCHEME_HTTPS,
            authority = Constants.AUTHORITY_READ_COMICS_ONLINE,
            paths = listOf(PathConstants.PATH_SEARCH),
            queryParams = mapOf(KEY_QUERY to query)
        ).toUrl()
        GetResponseAsync(MangasResponseHandler(), callback).execute(requestUrl)
    }

    companion object {
        const val KEY_QUERY = "query"
    }
}
