package com.sun.mangareader01.data.model

import org.json.JSONObject

const val JSON_KEY_NAME = "name"
const val JSON_KEY_ID = "id"

data class Category(
    val name: String,
    val id: Int,
    val mangas: MutableList<Manga> = mutableListOf()
) {
    constructor(jsonObject: JSONObject) : this(
        name = jsonObject.optString(JSON_KEY_NAME),
        id = jsonObject.optInt(JSON_KEY_ID)
    )
}
