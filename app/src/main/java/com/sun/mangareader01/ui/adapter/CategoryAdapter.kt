package com.sun.mangareader01.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.mangareader01.R
import com.sun.mangareader01.data.model.Category
import com.sun.mangareader01.ui.listener.OnItemClickListener
import kotlinx.android.synthetic.main.item_category.view.recyclerMangas
import kotlinx.android.synthetic.main.item_category.view.textCategoryName

class CategoryAdapter(
    private val categories: MutableList<Category>
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>(),
    CustomAdapter<Category> {

    override var onItemClickListener: OnItemClickListener? = null

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun updateData(data: List<Category>) {
        val diffUtil = DiffUtil.calculateDiff(
            CategoriesUpdateCallback(categories, data)
        )
        loadNewCategories(data)
        diffUtil.dispatchUpdatesTo(this)
    }

    private fun loadNewCategories(data: List<Category>) {
        categories.clear()
        categories.addAll(data)
    }

    override fun <T> updateValue(value: T) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category, parent, false),
            onItemClickListener,
            viewPool
        )

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindData(categories[position])

    class ViewHolder(
        view: View,
        clickListener: OnItemClickListener?,
        viewPool: RecyclerView.RecycledViewPool
    ) : RecyclerView.ViewHolder(view) {

        private var category: Category? = null
        private val mangaAdapter: HorizontalMangaAdapter by lazy {
            HorizontalMangaAdapter(mutableListOf())
        }
        private val textCategoryName: TextView by lazy { view.textCategoryName }
        private val recyclerMangas: RecyclerView by lazy { view.recyclerMangas }

        init {
            mangaAdapter.onItemClickListener = clickListener
            recyclerMangas.apply {
                layoutManager = LinearLayoutManager(
                    itemView.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = mangaAdapter
                setRecycledViewPool(viewPool)
            }
        }

        fun bindData(category: Category) {
            this.category = category
            textCategoryName.text = category.name
            mangaAdapter.updateData(category.mangas)
        }
    }

    class CategoriesUpdateCallback(
        private val oldCategories: List<Category>,
        private val newCategories: List<Category>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldCategories.size

        override fun getNewListSize() = newCategories.size

        override fun areItemsTheSame(oldPosition: Int, newPosition: Int) =
            oldCategories[oldPosition].id == newCategories[newPosition].id

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int) =
            areItemsTheSame(oldPosition, newPosition)
    }
}
