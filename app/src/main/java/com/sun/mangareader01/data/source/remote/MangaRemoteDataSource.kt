package com.sun.mangareader01.data.source.remote

import com.sun.mangareader01.data.model.*
import com.sun.mangareader01.data.source.MangaDataSource
import com.sun.mangareader01.data.source.MangaDataSource.Remote.Companion.SORT_BY_LAST_RELEASE
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback
import com.sun.mangareader01.utils.Constants.AUTHORITY_READ_COMICS_ONLINE
import com.sun.mangareader01.utils.Constants.SCHEME_HTTPS
import com.sun.mangareader01.utils.PathConstants
import com.sun.mangareader01.utils.PathConstants.PATH_COMIC
import com.sun.mangareader01.utils.PathConstants.PATH_FILTER_LIST

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
        callback: OnLoadedDataCallback<MangaDetail>
    ) {
        val requestUrl = DataRequest(
            scheme = SCHEME_HTTPS,
            authority = AUTHORITY_READ_COMICS_ONLINE,
            paths = listOf(PATH_COMIC, manga.slug)
        ).toUrl()
        GetResponseAsync(MangaDetailHandler(), callback).execute(requestUrl)
    }

    override fun getHotMangas(callback: OnLoadedDataCallback<MangasResponse>) {
        val requestUrl = DataRequest(
            scheme = SCHEME_HTTPS,
            authority = AUTHORITY_READ_COMICS_ONLINE
        ).toUrl()
        GetResponseAsync(HotMangasResponseHandler(), callback).execute(
            requestUrl
        )
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
        val requestUrl = DataRequest(
            scheme = SCHEME_HTTPS,
            authority = AUTHORITY_READ_COMICS_ONLINE,
            paths = listOf(PATH_FILTER_LIST),
            queryParams = mapOf(
                KEY_PAGE to page,
                KEY_CATEGORY to category,
                KEY_ALPHA to alpha,
                KEY_SORT_BY to sortBy,
                KEY_ASC to asc,
                KEY_AUTHOR to author,
                KEY_TAG to tag
            )
        ).toUrl()
        GetResponseAsync(FilteredMangasResponseHandler(), callback)
            .execute(requestUrl)
    }

    override fun getMostViewedMangas(
        callback: OnLoadedDataCallback<MangasResponse>
    ) = getFilteredMangas(
        sortBy = MangaDataSource.Remote.SORT_BY_VIEWS,
        asc = false,
        callback = callback
    )

    override fun getLastReleasedMangas(
        callback: OnLoadedDataCallback<MangasResponse>
    ) = getFilteredMangas(
        sortBy = SORT_BY_LAST_RELEASE,
        asc = false,
        callback = callback
    )

    override fun getPages(
        chapter: Chapter,
        callback: OnLoadedDataCallback<PagesResponse>
    ) {
        GetResponseAsync(PagesResponseHandler(), callback).execute(chapter.url)
    }

    companion object {
        const val KEY_QUERY = "query"
        const val KEY_PAGE = "page"
        const val KEY_CATEGORY = "cat"
        const val KEY_ALPHA = "alpha"
        const val KEY_SORT_BY = "sortBy"
        const val KEY_ASC = "asc"
        const val KEY_AUTHOR = "author"
        const val KEY_TAG = "tag"
    }
}
