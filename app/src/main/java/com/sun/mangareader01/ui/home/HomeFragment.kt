package com.sun.mangareader01.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sun.mangareader01.R
import com.sun.mangareader01.data.model.Category
import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.model.MangaDetail
import com.sun.mangareader01.data.source.repository.MangaRepository
import com.sun.mangareader01.ui.adapter.CategoryAdapter
import com.sun.mangareader01.ui.listener.OnItemClickListener
import com.sun.mangareader01.utils.Extensions.setImageUrl
import com.sun.mangareader01.utils.Extensions.showToast
import com.sun.mangareader01.utils.Helpers.buildCoverUrl
import kotlinx.android.synthetic.main.fragment_home.layoutRandomManga
import kotlinx.android.synthetic.main.fragment_home.recyclerCategories
import kotlinx.android.synthetic.main.item_manga.imageMangaItemCover
import kotlinx.android.synthetic.main.item_manga.textMangaItemTitle

class HomeFragment : Fragment(), HomeContract.View {

    private var onItemClickListener: OnItemClickListener? = null
    private var randomManga: Manga? = null

    private val categoriesList = ArrayList<Category>()
    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter(mutableListOf())
    }
    private val presenter: HomeContract.Presenter by lazy {
        HomePresenter(this, MangaRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            onItemClickListener = it.getParcelable(BUNDLE_CLICK_LISTENER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryAdapter.onItemClickListener = onItemClickListener
        recyclerCategories.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = categoryAdapter
        }
        layoutRandomManga.setOnClickListener {
            onItemClickListener?.onMangaClick(randomManga)
        }
        presenter.getRandomMangaDetail()
        presenter.getCategories()
    }

    override fun showRandomMangaDetail(mangaDetail: MangaDetail) {
        randomManga = mangaDetail.getManga()
        randomManga?.also {
            textMangaItemTitle?.text = it.title
            imageMangaItemCover?.setImageUrl(buildCoverUrl(it.slug))
        }
    }

    override fun showCategories(categories: List<Category>) {
        categoriesList.clear()
        categoriesList.addAll(categories)
        categoryAdapter.updateData(categoriesList)
        presenter.getMangas(
            START_POSITION_ADAPTER,
            categories[START_POSITION_ADAPTER]
        )
    }

    override fun updateMangasCategory(position: Int, category: Category) {
        categoriesList[position] = category
        categoryAdapter.notifyItemChanged(position)
        val nextPosition = position + 1
        if (nextPosition < categoryAdapter.itemCount) {
            presenter.getMangas(nextPosition, categoriesList[nextPosition])
        }
    }

    override fun showError(exception: Exception) {
        context?.showToast(exception.toString())
    }

    companion object {
        private const val BUNDLE_CLICK_LISTENER = "clickListener"
        private const val START_POSITION_ADAPTER = 0

        @JvmStatic
        fun newInstance(clickListener: OnItemClickListener) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_CLICK_LISTENER, clickListener)
                }
            }
    }
}
