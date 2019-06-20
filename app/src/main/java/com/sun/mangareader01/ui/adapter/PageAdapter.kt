package com.sun.mangareader01.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sun.mangareader01.R
import com.sun.mangareader01.ui.listener.OnItemClickListener
import com.sun.mangareader01.utils.Extensions.setImageUrl
import kotlinx.android.synthetic.main.item_chapter_page.view.imagePage


class PageAdapter(
    private val pageUrls: MutableList<String>,
    private val clickListener: View.OnClickListener
) : RecyclerView.Adapter<PageAdapter.ViewHolder>(),
    CustomAdapter<String> {

    override var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chapter_page, parent, false)
        return ViewHolder(view, clickListener)
    }

    override fun getItemCount() = pageUrls.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(pageUrls[position])
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun updateData(data: List<String>) {
        val diffUtil = DiffUtil.calculateDiff(UpdateCallback(pageUrls, data))
        loadNewPageUrls(data)
        diffUtil.dispatchUpdatesTo(this)
    }

    private fun loadNewPageUrls(newPageUrls: List<String>) {
        pageUrls.clear()
        pageUrls.addAll(newPageUrls)
    }

    override fun <T> updateValue(value: T) {
    }

    class ViewHolder(view: View, clickListener: View.OnClickListener) :
        RecyclerView.ViewHolder(view) {

        private val imagePage: ImageView by lazy { view.imagePage }

        init {
            imagePage.setOnClickListener(clickListener)
        }

        fun bindData(url: String) {
            imagePage.setImageUrl(url)
        }
    }

    class UpdateCallback(
        private val oldPageUrls: List<String>,
        private val newPageUrls: List<String>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldPageUrls.size

        override fun getNewListSize() = newPageUrls.size

        override fun areItemsTheSame(oldPosition: Int, newPosition: Int) =
            oldPageUrls[oldPosition] == newPageUrls[newPosition]

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int) =
            areItemsTheSame(oldPosition, newPosition)
    }
}
