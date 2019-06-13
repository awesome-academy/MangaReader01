package com.sun.mangareader01.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sun.mangareader01.R
import com.sun.mangareader01.data.model.Chapter
import kotlinx.android.synthetic.main.item_chapter.view.textChapterTitle
import kotlinx.android.synthetic.main.item_chapter.view.textUploadDate

class ChapterAdapter(
    private val chapters: MutableList<Chapter>
) : RecyclerView.Adapter<ChapterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chapter, parent, false))

    override fun getItemCount() = chapters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindData(chapters[position])

    fun updateData(newChapters: List<Chapter>) {
        val diffUtil = DiffUtil.calculateDiff(ChaptersUpdateCallback(chapters, newChapters))
        loadNewChapters(newChapters)
        diffUtil.dispatchUpdatesTo(this)
    }

    private fun loadNewChapters(newChapters: List<Chapter>) {
        chapters.clear()
        chapters.addAll(newChapters)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textChapterTitle: TextView  by lazy { itemView.textChapterTitle }
        private val textUploadDate: TextView  by lazy { itemView.textUploadDate }

        fun bindData(chapter: Chapter) {
            textChapterTitle.text = chapter.title
            textUploadDate.text = chapter.uploadDate
            itemView.setOnClickListener {
            }
        }
    }

    class ChaptersUpdateCallback(
        private val oldChapters: List<Chapter>,
        private val newChapters: List<Chapter>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldChapters.size

        override fun getNewListSize() = newChapters.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldChapters[oldItemPosition].url == newChapters[newItemPosition].url

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            areItemsTheSame(oldItemPosition, newItemPosition)
    }
}
