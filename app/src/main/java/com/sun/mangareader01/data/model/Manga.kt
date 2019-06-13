package com.sun.mangareader01.data.model

import org.json.JSONObject
import java.io.Serializable

const val JSON_KEY_TITLE = "value"
const val JSON_KEY_SLUG = "data"

data class Manga(val title: String, val slug: String) : Serializable {
    constructor(jsonObject: JSONObject) : this(
        title = jsonObject.optString(JSON_KEY_TITLE),
        slug = jsonObject.optString(JSON_KEY_SLUG)
    )
}
