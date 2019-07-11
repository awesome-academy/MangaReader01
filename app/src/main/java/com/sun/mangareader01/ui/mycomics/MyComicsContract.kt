package com.sun.mangareader01.ui.mycomics

import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.model.MangaDetail

interface MyComicsContract {

    interface View {
        fun showMangas(mangas: List<Manga>)
        fun showError(exception: Exception)
        fun downloadMangaFiles(mangaDetail: MangaDetail)
        fun deleteMangaFiles(manga: Manga)
        fun showNoticeLoadPageUrls()
    }

    interface Presenter {
        fun getMyMangas()
        fun deleteManga(manga: Manga)
        fun updateManga(manga: Manga)
        fun saveManga(manga: Manga)
        fun getPageUrlsThenDownload(mangaDetail: MangaDetail)
    }
}
