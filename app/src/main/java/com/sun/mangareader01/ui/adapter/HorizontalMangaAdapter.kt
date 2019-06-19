package com.sun.mangareader01.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sun.mangareader01.R
import com.sun.mangareader01.data.model.Manga

class HorizontalMangaAdapter(mangas: MutableList<Manga>) :
    MangaAdapter(mangas) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_manga_cover_2, parent, false),
            onItemClickListener
        )
}
