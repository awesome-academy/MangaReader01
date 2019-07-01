package com.sun.mangareader01.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sun.mangareader01.R
import com.sun.mangareader01.data.model.Chapter
import com.sun.mangareader01.ui.listener.OnItemClickListener
import kotlinx.android.synthetic.main.item_chapter.view.textChapterTitle
import kotlinx.android.synthetic.main.item_chapter.view.textUploadDate

class ChapterAdapter(
    private val chapters: MutableList<Chapter>
) : RecyclerView.Adapter<ChapterAdapter.ViewHolder>(),
    CustomAdapter<Chapter> {

    override var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_chapter,
                parent,
                false
            ), onItemClickListener
        )

    override fun getItemCount() = chapters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindData(chapters[position])

    override fun updateData(data: List<Chapter>) {
        val diffUtil = DiffUtil.calculateDiff(
            ChaptersUpdateCallback(chapters, data)
        )
        loadNewChapters(data)
        diffUtil.dispatchUpdatesTo(this)
    }

    override fun <T> updateValue(value: T) {
    }

    private fun loadNewChapters(newChapters: List<Chapter>) {
        chapters.clear()
        chapters.addAll(newChapters)
    }

    class ViewHolder(view: View, clickListener: OnItemClickListener?) :
        RecyclerView.ViewHolder(view) {

        private var item: Chapter? = null
        private val textChapterTitle: TextView  by lazy { view.textChapterTitle }
        private val textUploadDate: TextView  by lazy { view.textUploadDate }

        init {
            view.setOnClickListener { clickListener?.onChapterClick(item) }
        }

        fun bindData(chapter: Chapter) {
            item = chapter
            textChapterTitle.text = chapter.title
            textUploadDate.text = chapter.uploadDate
        }
    }

    class ChaptersUpdateCallback(
        private val oldChapters: List<Chapter>,
        private val newChapters: List<Chapter>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldChapters.size

        override fun getNewListSize() = newChapters.size

        override fun areItemsTheSame(oldPosition: Int, newPosition: Int) =
            oldChapters[oldPosition].url == newChapters[newPosition].url

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int) =
            areItemsTheSame(oldPosition, newPosition)
    }
}
