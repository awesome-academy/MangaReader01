package com.sun.mangareader01.data.model

import com.sun.mangareader01.utils.Extensions.parse
import org.json.JSONObject

const val JSON_KEY_PAGES = "pages"

data class PagesResponse(val pageUrls: List<String>) {
    constructor(jsonObject: JSONObject) : this(
        pageUrls = ArrayList<String>().parse(
            jsonObject.optJSONArray(JSON_KEY_PAGES)
        )
    )
}
