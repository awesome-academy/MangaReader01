package com.sun.mangareader01.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.mangareader01.R
import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.source.repository.MangaRepository
import com.sun.mangareader01.ui.adapter.CustomAdapter
import com.sun.mangareader01.ui.adapter.MangaAdapter
import com.sun.mangareader01.ui.listener.OnItemClickListener
import com.sun.mangareader01.utils.Constants.EMPTY_STRING
import com.sun.mangareader01.utils.Extensions.showToast
import kotlinx.android.synthetic.main.fragment_search.barSearching
import kotlinx.android.synthetic.main.fragment_search.recyclerSearchResult

class SearchFragment : Fragment(),
    SearchContract.View {

    var clickListener: OnItemClickListener? = null
    private var keyword = EMPTY_STRING
    private val presenter: SearchContract.Presenter by lazy {
        SearchPresenter(this, MangaRepository)
    }
    private val mangaAdapter: CustomAdapter<Manga> by lazy {
        MangaAdapter(mutableListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            keyword = it.getString(BUNDLE_SEARCH_KEY) ?: keyword
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getMangas(keyword)
        setUpSearchResultView()
        getMangas(keyword)
        mangaAdapter.onItemClickListener = clickListener
    }

    fun getMangas(keyword: String) {
        presenter.getMangas(keyword)
        displaySearchingBar()
    }

    override fun showMangas(mangas: List<Manga>) {
        mangaAdapter.updateData(mangas)
        hideSearchingBar()
        recyclerSearchResult?.scrollToPosition(0)
    }

    override fun showError(exception: Exception) {
        context?.showToast(exception.toString())
        hideSearchingBar()
    }

    private fun setUpSearchResultView() {
        recyclerSearchResult.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = mangaAdapter as RecyclerView.Adapter<*>
            itemAnimator = DefaultItemAnimator()
        }
    }

    private fun displaySearchingBar() {
        barSearching?.visibility = View.VISIBLE
    }

    private fun hideSearchingBar() {
        barSearching?.visibility = View.GONE
    }

    companion object {
        private const val BUNDLE_SEARCH_KEY = "keyword"

        @JvmStatic
        fun newInstance(keyword: String?, clickListener: OnItemClickListener) =
            SearchFragment().apply {
                this.clickListener = clickListener
                arguments = Bundle().apply {
                    putString(BUNDLE_SEARCH_KEY, keyword)
                }
            }
    }
}
