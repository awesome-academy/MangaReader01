package com.sun.mangareader01.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sun.mangareader01.R
import kotlinx.android.synthetic.main.item_tag.view.textTag

class TagAdapter(
    private val tags: List<String>
) : RecyclerView.Adapter<TagAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_tag, parent, false))

    override fun getItemCount() = tags.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindData(tags[position])

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textTag: TextView = itemView.textTag

        fun bindData(tag: String) {
            textTag.text = tag
            itemView.setOnClickListener {
            }
        }
    }
}
