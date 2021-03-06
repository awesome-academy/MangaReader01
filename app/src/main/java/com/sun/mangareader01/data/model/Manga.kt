package com.sun.mangareader01.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

const val JSON_KEY_TITLE = "value"
const val JSON_KEY_SLUG = "data"

@Parcelize
data class Manga(
    val title: String,
    val slug: String,
    var saved: Boolean = false
) : Parcelable {
    constructor(jsonObject: JSONObject) : this(
        title = jsonObject.optString(JSON_KEY_TITLE),
        slug = jsonObject.optString(JSON_KEY_SLUG)
    )
}
