package com.sun.mangareader01.data.source.remote

import com.sun.mangareader01.data.model.MangaDetail
import com.sun.mangareader01.utils.ApiHelpers.ApiMangaDetail
import org.json.JSONObject
import org.jsoup.Jsoup

class MangaDetailHandler : DataResponseHandler<MangaDetail> {
    override fun parseToObject(string: String): MangaDetail {
        val jsonString = ApiMangaDetail(Jsoup.parse(string)).getJsonString()
        return MangaDetail(JSONObject(jsonString))
    }
}
