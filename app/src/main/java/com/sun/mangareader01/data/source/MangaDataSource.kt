package com.sun.mangareader01.data.source

import com.sun.mangareader01.data.model.*
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback
import com.sun.mangareader01.utils.Constants.EMPTY_STRING

interface MangaDataSource {
    interface Remote {
        fun getMangas(
            query: String,
            callback: OnLoadedDataCallback<MangasResponse>
        )

        fun getMangaDetail(
            manga: Manga,
            callback: OnLoadedDataCallback<MangaDetail>
        )

        fun getRandomMangaDetail(callback: OnLoadedDataCallback<MangaDetail>)

        fun getHotMangas(callback: OnLoadedDataCallback<MangasResponse>)

        fun getFilteredMangas(
            page: Int = 1,
            category: String = EMPTY_STRING,
            alpha: String = EMPTY_STRING,
            sortBy: String = SORT_BY_VIEWS,
            asc: Boolean = true,
            author: String = EMPTY_STRING,
            tag: String = EMPTY_STRING,
            callback: OnLoadedDataCallback<MangasResponse>
        )

        fun getMostViewedMangas(
            callback: OnLoadedDataCallback<MangasResponse>
        )

        fun getLastReleasedMangas(
            callback: OnLoadedDataCallback<MangasResponse>
        )

        fun getPages(
            chapter: Chapter,
            callback: OnLoadedDataCallback<PagesResponse>
        )

        fun getCategories(
            callback: OnLoadedDataCallback<CategoriesResponse>
        )

        companion object {
            const val SORT_BY_VIEWS = "views"
            const val SORT_BY_LAST_RELEASE = "last_release"
            const val SORT_BY_NAME = "name"
        }
    }

    interface Local {

        fun getMyMangas(
            callback: OnLoadedDataCallback<MangasResponse>
        )

        fun insertMangas(
            manga: Manga,
            callback: OnLoadedDataCallback<Boolean>
        )

        fun deleteManga(
            manga: Manga,
            callback: OnLoadedDataCallback<Boolean>
        )
    }
}
