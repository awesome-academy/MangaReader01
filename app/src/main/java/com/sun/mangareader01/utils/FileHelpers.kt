package com.sun.mangareader01.utils

import android.content.Context
import android.net.Uri
import com.sun.mangareader01.data.model.Chapter
import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.utils.FileHelpers.DirFormat.PATH_SAVE_CHAPTER
import com.sun.mangareader01.utils.FileHelpers.DirFormat.PATH_SAVE_MANGA
import java.io.File

object FileHelpers {

    fun getFilePath(context: Context): String = context.filesDir.path

    fun getMangaDir(context: Context, manga: Manga): File {
        val mangaUri = PATH_SAVE_MANGA.format(getFilePath(context), manga.slug)
        return File(mangaUri)
    }

    fun getChapterDir(context: Context, chapter: Chapter): File {
        val mangaSlug = chapter.getMangaSlug()
        val chapterSlug = Uri.parse(chapter.url).lastPathSegment
        val chapterUri = PATH_SAVE_CHAPTER.format(
            getFilePath(context), mangaSlug, chapterSlug
        )
        return File(chapterUri)
    }

    fun getPageUrls(chapterDir: File): List<String> {
        val files = chapterDir.listFiles()
        val urls = ArrayList<String>()
        for (file in files) urls.add(file.path)
        return urls
    }

    object DirFormat {
        const val PATH_SAVE_MANGA = "%s/save/%s"
        const val PATH_SAVE_CHAPTER = "$PATH_SAVE_MANGA/%s"
    }
}
