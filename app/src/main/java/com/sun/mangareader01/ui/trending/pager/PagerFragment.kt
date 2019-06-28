package com.sun.mangareader01.ui.trending.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sun.mangareader01.R
import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.ui.adapter.CustomAdapter
import com.sun.mangareader01.ui.adapter.MangaAdapter
import com.sun.mangareader01.ui.listener.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_trending_pager.layoutRefresh
import kotlinx.android.synthetic.main.fragment_trending_pager.recyclerMangas

abstract class PagerFragment : Fragment(),
    SwipeRefreshLayout.OnRefreshListener {

    abstract val title: String?
    abstract val icon: Int?
    private var onItemClickListener: OnItemClickListener? = null
    private val mangaAdapter: CustomAdapter<Manga> by lazy {
        MangaAdapter(mutableListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            onItemClickListener = it.getParcelable(BUNDLE_CLICK_LISTENER_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_trending_pager, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayLoadingBar()
        recyclerMangas.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mangaAdapter as RecyclerView.Adapter<*>
        }
        mangaAdapter.onItemClickListener = onItemClickListener
        layoutRefresh.setOnRefreshListener(this)
    }

    private fun showMangas(mangas: List<Manga>) {
        mangaAdapter.updateData(mangas)
        hideLoadingBar()
    }

    override fun onRefresh() {
    }

    private fun displayLoadingBar() {
        layoutRefresh?.isRefreshing = true
    }

    private fun hideLoadingBar() {
        layoutRefresh?.isRefreshing = false
    }

    companion object {
        const val BUNDLE_CLICK_LISTENER_KEY = "onItemClickListener"
    }
}
