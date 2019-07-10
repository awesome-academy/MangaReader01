package com.sun.mangareader01.data.source.local

import android.content.Context
import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.model.MangasResponse
import com.sun.mangareader01.data.source.MangaDataSource

class MangaLocalDataSource(context: Context) : MangaDataSource.Local {

    private val database = DatabaseHelper(context)

    override fun getMyMangas(callback: OnLoadedDataCallback<MangasResponse>) {
        LoadDataAsync(callback).execute(MangasResponse(database.mangas))
    }

    override fun insertManga(
        manga: Manga,
        callback: OnLoadedDataCallback<Boolean>
    ) {
        LoadDataAsync(callback).execute(database.insertManga(manga))
    }

    override fun updateManga(
        manga: Manga,
        callback: OnLoadedDataCallback<Boolean>
    ) {
        LoadDataAsync(callback).execute(database.updateManga(manga))
    }

    override fun deleteManga(
        manga: Manga,
        callback: OnLoadedDataCallback<Boolean>
    ) {
        LoadDataAsync(callback).execute(database.deleteManga(manga))
    }
}
