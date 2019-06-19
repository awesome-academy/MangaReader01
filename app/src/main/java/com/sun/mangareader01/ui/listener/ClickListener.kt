package com.sun.mangareader01.ui.listener

import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.ui.adapter.CustomAdapter

interface ClickListener {
    interface OnMangaClickListener {
        var onMangaClickListener: CustomAdapter.OnItemClickListener<Manga>?
    }
}
