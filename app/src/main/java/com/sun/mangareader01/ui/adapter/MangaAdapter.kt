package com.sun.mangareader01.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sun.mangareader01.R
import com.sun.mangareader01.data.model.Manga
import kotlinx.android.synthetic.main.item_manga.view.imageComicCover
import kotlinx.android.synthetic.main.item_manga.view.textComicDetails
import kotlinx.android.synthetic.main.item_manga.view.textComicTitle

class MangaAdapter(
    private val context: Context,
    private val mangas: List<Manga>
) : RecyclerView.Adapter<MangaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_manga, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindData(mangas[position])

    override fun getItemCount() = mangas.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textComicTitle: TextView = view.textComicTitle
        private val textComicDetails: TextView = view.textComicDetails
        private val imageComicCover: ImageView = view.imageComicCover

        // Bind data, imageComicCover set default image
        fun bindData(manga: Manga) {
            textComicTitle.text = manga.title
            textComicDetails.text = manga.slug
            imageComicCover.setImageResource(R.mipmap.ic_launcher)
        }
    }
}
