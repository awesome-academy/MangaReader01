package com.sun.mangareader01.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sun.mangareader01.R
import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.ui.listener.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_trending_pager.layoutRefresh
import kotlinx.android.synthetic.main.fragment_trending_pager.recyclerMangas

class TrendingPagerAdapter(
    fragmentManager: FragmentManager,
    private val fragments: List<PagerFragment>
) : FragmentStatePagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int) = getItem(position).title

    class PagerFragment(
        val title: String,
        val icon: Int,
        private val clickListener: OnItemClickListener,
        private val refreshListener: SwipeRefreshLayout.OnRefreshListener
    ) : Fragment() {

        private val mangaAdapter: CustomAdapter<Manga> by lazy {
            MangaAdapter(mutableListOf())
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
            mangaAdapter.onItemClickListener = clickListener
            layoutRefresh.setOnRefreshListener(refreshListener)
        }

        fun updateData(data: List<Manga>) {
            mangaAdapter.updateData(data)
            hideLoadingBar()
        }

        private fun displayLoadingBar() {
            layoutRefresh?.isRefreshing = true
        }

        private fun hideLoadingBar() {
            layoutRefresh?.isRefreshing = false
        }
    }
}
