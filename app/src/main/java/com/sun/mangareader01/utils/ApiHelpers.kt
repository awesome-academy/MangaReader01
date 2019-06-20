package com.sun.mangareader01.utils

import com.sun.mangareader01.data.model.*
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class ApiHelpers {

    class ApiMangaDetail(document: Document) : ApiAdapter<MangaDetail> {

        private val body = document.body()

        override fun getJsonObject() = JSONObject().apply {
            put(JSON_KEY_TITLE, firstText(MANGA_TITLE))
            put(JSON_KEY_AUTHOR, firstText(MANGA_AUTHOR))
            put(JSON_KEY_SUMMARY, firstText(MANGA_SUMMARY))
            put(JSON_KEY_RATING, getRating())
            put(JSON_KEY_TAGS, getTags())
            put(JSON_KEY_CHAPTERS, getChapters())
        }

        private fun firstText(cssQuery: String) =
            body.getFirstText(cssQuery)

        private fun getRating() =
            body.selectFirst(MANGA_RATING).attr(ATTR_DATA_SCORE)

        private fun getTags() = JSONArray().apply {
            body.select(MANGA_TAGS).forEach { put(it.text().trim()) }
        }

        private fun getChapters() = JSONArray().apply {
            body.select(MANGA_CHAPTER).forEach { put(getChapter(it)) }
        }

        private fun getChapter(element: Element) = JSONObject().apply {
            element.also {
                put(JSON_KEY_TITLE, it.getFirstText(MANGA_CHAPTER_TITLE))
                put(JSON_KEY_URL, it.select(MANGA_CHAPTER_URL).attr(ATTR_HREF))
                put(JSON_KEY_DATE, it.getFirstText(MANGA_UPLOAD_DATE))
            }
        }

        companion object CssQuery {
            const val ATTR_HREF = "href"
            const val ATTR_DATA_SCORE = "data-score"
            const val MANGA_TITLE = "h2.listmanga-header"
            const val MANGA_SUMMARY = "div.manga.well p"
            const val MANGA_RATING = "#item-rating"
            const val MANGA_AUTHOR = ".dl-horizontal a[href*=\"author\"]"
            const val MANGA_TAGS = ".tag-links a"
            const val MANGA_CHAPTER = ".chapters li"
            const val MANGA_CHAPTER_TITLE = ".chapter-title-rtl"
            const val MANGA_CHAPTER_URL = ".chapter-title-rtl > a"
            const val MANGA_UPLOAD_DATE = ".date-chapter-title-rtl"
        }
    }
}
