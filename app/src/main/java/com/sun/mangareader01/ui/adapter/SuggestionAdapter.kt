package com.sun.mangareader01.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.sun.mangareader01.R
import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.utils.Constants.EMPTY_STRING
import com.sun.mangareader01.utils.Extensions.setImageUrl
import com.sun.mangareader01.utils.Helpers
import kotlinx.android.synthetic.main.item_suggestion.view.imageMangaThumb
import kotlinx.android.synthetic.main.item_suggestion.view.textComicTitle

class SuggestionAdapter(
    private val suggestions: MutableList<Manga>
) : BaseAdapter(),
    CustomAdapter<Manga> {

    override var onItemClickListener:
        CustomAdapter.OnItemClickListener<Manga>? = null
    private var keyword = EMPTY_STRING

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val view = convertView
            ?: View.inflate(parent.context, R.layout.item_suggestion, null)
        view.tag = ViewHolder(view, getItem(position), keyword)
        return view
    }

    override fun getItem(position: Int) = suggestions[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = suggestions.size

    override fun updateData(data: List<Manga>) {
        suggestions.clear()
        suggestions.addAll(data)
        notifyDataSetChanged()
    }

    override fun <String> updateValue(value: String) {
        keyword = value.toString()
    }

    private class ViewHolder(view: View) {

        val textMangaTitle: TextView by lazy { view.textComicTitle }
        val imageMangaThumb: ImageView by lazy { view.imageMangaThumb }

        constructor(view: View, manga: Manga, keyword: String) : this(view) {
            textMangaTitle.text = Helpers.highlightKeyword(manga.title, keyword)
            imageMangaThumb.setImageUrl(
                url = Helpers.buildThumbUrl(manga.slug),
                circleCrop = true
            )
        }
    }
}
