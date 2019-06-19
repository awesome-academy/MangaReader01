package com.sun.mangareader01.data.model

import com.sun.mangareader01.utils.Extensions.parse
import com.sun.mangareader01.utils.Extensions.parseChapters
import org.json.JSONObject

const val JSON_KEY_AUTHOR = "author"
const val JSON_KEY_TAGS = "tags"
const val JSON_KEY_SUMMARY = "summary"
const val JSON_KEY_RATING = "rating"
const val JSON_KEY_CHAPTERS = "chapters"

data class MangaDetailResponse(
    val title: String,
    val author: String,
    val summary: String,
    val rating: Float,
    val tags: List<String>,
    val chapters: List<Chapter>
) {
    constructor(jsonObject: JSONObject) : this(
        title = jsonObject.optString(JSON_KEY_TITLE),
        author = jsonObject.optString(JSON_KEY_AUTHOR),
        summary = jsonObject.optString(JSON_KEY_SUMMARY),
        rating = jsonObject.optDouble(JSON_KEY_RATING).toFloat(),
        tags = ArrayList<String>().parse(
            jsonObject.optJSONArray(JSON_KEY_TAGS)
        ),
        chapters = ArrayList<Chapter>().parseChapters(
            jsonObject.optJSONArray(JSON_KEY_CHAPTERS)
        )
    )
}
