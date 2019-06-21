package com.sun.mangareader01.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sun.mangareader01.R
import com.sun.mangareader01.ui.listener.OnItemClickListener
import kotlinx.android.synthetic.main.item_tag.view.textTag

class TagAdapter(
    private val tags: MutableList<String>
) : RecyclerView.Adapter<TagAdapter.ViewHolder>(),
    CustomAdapter<String> {

    override var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tag, parent, false)
        return ViewHolder(view, onItemClickListener)
    }

    override fun updateData(data: List<String>) {
        tags.clear()
        tags.addAll(data)
    }

    override fun <T> updateValue(value: T) {
    }

    override fun getItemCount() = tags.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindData(tags[position])

    class ViewHolder(view: View, clickListener: OnItemClickListener?) :
        RecyclerView.ViewHolder(view) {

        private var item: String? = null
        private val textTag: TextView by lazy { view.textTag }

        init {
            view.setOnClickListener { clickListener?.onTagClick(item) }
        }

        fun bindData(tag: String) {
            item = tag
            textTag.text = tag
        }
    }
}
