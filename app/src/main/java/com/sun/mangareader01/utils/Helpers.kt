package com.sun.mangareader01.utils

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import com.sun.mangareader01.data.model.DataRequest
import com.sun.mangareader01.utils.Constants.AUTHORITY_READ_COMICS_ONLINE
import com.sun.mangareader01.utils.Constants.SCHEME_HTTPS
import com.sun.mangareader01.utils.PathConstants.PATH_COVER
import com.sun.mangareader01.utils.PathConstants.PATH_COVER_FILE_NAME
import com.sun.mangareader01.utils.PathConstants.PATH_MANGA
import com.sun.mangareader01.utils.PathConstants.PATH_THUMB_FILE_NAME
import com.sun.mangareader01.utils.PathConstants.PATH_UPLOADS

object Helpers {
    fun buildCoverUrl(slug: String) = DataRequest(
        scheme = SCHEME_HTTPS,
        authority = AUTHORITY_READ_COMICS_ONLINE,
        paths = listOf(
            PATH_UPLOADS, PATH_MANGA, slug,
            PATH_COVER, PATH_COVER_FILE_NAME
        )
    ).toUrl()

    fun buildThumbUrl(slug: String) = DataRequest(
        scheme = SCHEME_HTTPS,
        authority = AUTHORITY_READ_COMICS_ONLINE,
        paths = listOf(
            PATH_UPLOADS, PATH_MANGA, slug,
            PATH_COVER, PATH_THUMB_FILE_NAME
        )
    ).toUrl()

    fun highlightKeyword(
        fullString: String,
        keyword: String
    ): SpannableString {
        val keywordIndex = fullString.indexOf(keyword, 0, true)
        return SpannableString(fullString).apply {
            if (keywordIndex != -1) setSpan(
                BackgroundColorSpan(Color.YELLOW),
                keywordIndex, keywordIndex + keyword.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}
