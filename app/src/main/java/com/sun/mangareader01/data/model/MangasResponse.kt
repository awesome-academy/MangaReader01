package com.sun.mangareader01.data.model

import org.json.JSONObject

const val JSON_KEY_SUGGESTION = "suggestions"

data class MangasResponse(val mangas: List<Manga>) {
    constructor(jsonObject: JSONObject) : this(
        mangas = ArrayList<Manga>().apply {
            val jsonArray = jsonObject.optJSONArray(JSON_KEY_SUGGESTION)
            for (index in 0 until jsonArray.length()) {
                add(Manga(jsonArray.optJSONObject(index)))
            }
        }
    )
}
