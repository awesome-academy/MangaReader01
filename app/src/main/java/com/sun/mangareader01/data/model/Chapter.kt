package com.sun.mangareader01.data.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

const val JSON_KEY_URL = "url"
const val JSON_KEY_DATE = "date"

@Parcelize
data class Chapter(
    val title: String,
    val url: String,
    val uploadDate: String,
    val pageUrls: MutableList<String> = mutableListOf()
) : Parcelable {
    constructor(jsonObject: JSONObject) : this(
        title = jsonObject.optString(JSON_KEY_TITLE),
        url = jsonObject.optString(JSON_KEY_URL),
        uploadDate = jsonObject.optString(JSON_KEY_DATE)
    )

    fun getMangaSlug() = Uri.parse(url).pathSegments.run { get(size - 2) }
}
