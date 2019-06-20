package com.sun.mangareader01.data.source.remote

import com.sun.mangareader01.data.model.PagesResponse
import com.sun.mangareader01.utils.ApiHelpers
import org.json.JSONObject
import org.jsoup.Jsoup

class PagesResponseHandler : DataResponseHandler<PagesResponse> {
    override fun parseToObject(string: String): PagesResponse {
        val jsonString =
            ApiHelpers.ApiPages(Jsoup.parse(string)).getJsonString()
        return PagesResponse(JSONObject(jsonString))
    }
}
