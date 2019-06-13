package com.sun.mangareader01.data.source.remote

import com.sun.mangareader01.data.model.MangaDetailResponse
import com.sun.mangareader01.utils.ApiHelpers.ApiMangaDetail
import org.json.JSONObject
import org.jsoup.Jsoup

class MangaDetailResponseHandler : DataResponseHandler<MangaDetailResponse> {
    override fun parseToObject(string: String): MangaDetailResponse {
        val jsonString = ApiMangaDetail(Jsoup.parse(string)).getJsonString()
        return MangaDetailResponse(JSONObject(jsonString))
    }
}
