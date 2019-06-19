package com.sun.mangareader01.data.model

import org.json.JSONObject

const val JSON_KEY_CATEGORIES = "categories"

data class CategoriesResponse(val categories: List<Category>) {
    constructor(jsonObject: JSONObject) : this(
        categories = ArrayList<Category>().apply {
            val jsonArray = jsonObject.optJSONArray(JSON_KEY_CATEGORIES)
            for (index in 0 until jsonArray.length()) {
                add(Category(jsonArray.optJSONObject(index)))
            }
        }
    )
}
