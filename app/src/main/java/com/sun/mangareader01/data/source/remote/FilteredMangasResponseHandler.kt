package com.sun.mangareader01.data.source.remote

import com.sun.mangareader01.data.model.MangasResponse
import com.sun.mangareader01.utils.ApiHelpers
import org.json.JSONObject
import org.jsoup.Jsoup

class FilteredMangasResponseHandler : DataResponseHandler<MangasResponse> {
    override fun parseToObject(string: String): MangasResponse {
        val jsonString =
            ApiHelpers.ApiFilteredMangas(Jsoup.parse(string)).getJsonString()
        return MangasResponse(JSONObject(jsonString))
    }
}
