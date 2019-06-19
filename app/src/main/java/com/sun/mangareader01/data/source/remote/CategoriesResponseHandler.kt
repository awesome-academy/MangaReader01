package com.sun.mangareader01.data.source.remote

import com.sun.mangareader01.data.model.CategoriesResponse
import com.sun.mangareader01.utils.ApiHelpers
import org.json.JSONObject
import org.jsoup.Jsoup

class CategoriesResponseHandler : DataResponseHandler<CategoriesResponse> {
    override fun parseToObject(string: String): CategoriesResponse {
        val jsonString =
            ApiHelpers.ApiCategories(Jsoup.parse(string)).getJsonString()
        return CategoriesResponse(JSONObject(jsonString))
    }
}
