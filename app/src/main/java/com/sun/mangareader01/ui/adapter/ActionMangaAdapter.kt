package com.sun.mangareader01.ui.adapter

import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.ui.listener.OnMangaActionListener

interface ActionMangaAdapter : CustomAdapter<Manga> {
    var onMangaActionListener: OnMangaActionListener?
    var layoutViewResourceId: Int
}
