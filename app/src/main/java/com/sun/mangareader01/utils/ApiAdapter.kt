package com.sun.mangareader01.utils

import com.sun.mangareader01.utils.Constants.EMPTY_STRING
import org.json.JSONObject
import org.jsoup.nodes.Element

interface ApiAdapter<T> {
    fun getJsonObject(): JSONObject
    fun getJsonString() = getJsonObject().toString()
    fun Element.getFirstText(cssQuery: String) =
        selectFirst(cssQuery)?.text()?.trim() ?: EMPTY_STRING
}
