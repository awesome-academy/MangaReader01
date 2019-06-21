package com.sun.mangareader01.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sun.mangareader01.R
import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.ui.listener.OnItemClickListener
import com.sun.mangareader01.utils.Extensions.setImageUrl
import com.sun.mangareader01.utils.Helpers
import kotlinx.android.synthetic.main.item_manga.view.imageMangaItemCover
import kotlinx.android.synthetic.main.item_manga.view.textMangaItemTitle

class MangaAdapter(
    private val mangas: MutableList<Manga>
) : RecyclerView.Adapter<MangaAdapter.ViewHolder>(),
    CustomAdapter<Manga> {

    override var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_manga, parent, false),
            onItemClickListener
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindData(mangas[position])

    override fun getItemCount() = mangas.size

    override fun updateData(data: List<Manga>) {
        val diffUtil =
            DiffUtil.calculateDiff(MangasUpdateCallback(mangas, data))
        loadNewMangas(data)
        diffUtil.dispatchUpdatesTo(this)
    }

    override fun <String> updateValue(value: String) {
    }

    private fun loadNewMangas(newMangas: List<Manga>) {
        mangas.clear()
        mangas.addAll(newMangas)
    }

    class ViewHolder(view: View, clickListener: OnItemClickListener?) :
        RecyclerView.ViewHolder(view) {

        private var item: Manga? = null
        private val textSuggestionTitle: TextView by lazy {
            view.textMangaItemTitle
        }
        private val imageMangaItemCover: ImageView by lazy {
            view.imageMangaItemCover
        }

        init {
            view.setOnClickListener {
                item?.let { clickListener?.onMangaClick(it) }
            }
        }

        fun bindData(manga: Manga) {
            item = manga
            textSuggestionTitle.text = manga.title
            imageMangaItemCover.setImageUrl(Helpers.buildCoverUrl(manga.slug))
        }
    }

    class MangasUpdateCallback(
        private val oldMangas: List<Manga>,
        private val newMangas: List<Manga>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldMangas.size

        override fun getNewListSize() = newMangas.size

        override fun areItemsTheSame(oldPosition: Int, newPosition: Int) =
            oldMangas[oldPosition].slug == newMangas[newPosition].slug

        override fun areContentsTheSame(
            oldPosition: Int,
            newPosition: Int
        ) = areItemsTheSame(oldPosition, newPosition)
    }
}
