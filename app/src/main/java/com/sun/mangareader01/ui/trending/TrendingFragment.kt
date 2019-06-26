package com.sun.mangareader01.ui.trending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.tabs.TabLayout
import com.sun.mangareader01.R
import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.source.repository.MangaRepository
import com.sun.mangareader01.ui.adapter.TrendingPagerAdapter
import com.sun.mangareader01.ui.adapter.TrendingPagerAdapter.PagerFragment
import com.sun.mangareader01.ui.listener.OnItemClickListener
import com.sun.mangareader01.utils.Extensions.showToast
import kotlinx.android.synthetic.main.fragment_trending.pagerTrending
import kotlinx.android.synthetic.main.fragment_trending.tabTrending

class TrendingFragment : Fragment(),
    TabLayout.OnTabSelectedListener,
    TrendingContract.View,
    SwipeRefreshLayout.OnRefreshListener {

    private var clickListener: OnItemClickListener? = null
    private val presenter: TrendingContract.Presenter by lazy {
        TrendingPresenter(this, MangaRepository)
    }
    private val hotFragment: PagerFragment by lazy {
        PagerFragment.newInstance(
            getString(R.string.title_hot),
            R.drawable.ic_whatshot_black_24dp,
            clickListener,
            this
        )
    }
    private val mostViewedFragment: PagerFragment by lazy {
        PagerFragment.newInstance(
            getString(R.string.title_most_viewed),
            R.drawable.ic_grade_black_24dp,
            clickListener,
            this
        )
    }
    private val lastReleaseFragment: PagerFragment by lazy {
        PagerFragment.newInstance(
            getString(R.string.title_last_release),
            R.drawable.ic_new_releases_black_24dp,
            clickListener,
            this
        )
    }
    private val pagerFragments: List<PagerFragment> by lazy {
        listOf(hotFragment, mostViewedFragment, lastReleaseFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trending, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabPager()
        presenter.getTrending()
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
        childFragmentManager.apply {
            val transaction = beginTransaction()
            if (backStackEntryCount > 0) popBackStack()
            transaction.commit()
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        refreshPager(pagerFragments[tab.position])
    }

    override fun onRefresh() = refreshPager(getCurrentPager())

    private fun getCurrentPager() =
        pagerFragments[tabTrending.selectedTabPosition]

    private fun refreshPager(pagerFragment: PagerFragment?) {
        when (pagerFragment) {
            mostViewedFragment -> presenter.getMostViewedMangas()
            lastReleaseFragment -> presenter.getLastReleaseMangas()
            hotFragment -> presenter.getHotMangas()
        }
    }

    override fun showMostViewed(mangas: List<Manga>) =
        mostViewedFragment.updateData(mangas)

    override fun showLastRelease(mangas: List<Manga>) =
        lastReleaseFragment.updateData(mangas)

    override fun showHotMangas(mangas: List<Manga>) =
        hotFragment.updateData(mangas)

    override fun showError(exception: Exception) {
        context?.showToast(exception.toString())
    }

    private fun initTabPager() {
        pagerTrending.adapter = TrendingPagerAdapter(
            childFragmentManager,
            pagerFragments
        )
        tabTrending.apply {
            setupWithViewPager(pagerTrending, true)
            addOnTabSelectedListener(this@TrendingFragment)
            for (index in 0 until pagerFragments.size) {
                pagerFragments[index].icon?.let {
                    getTabAt(index)?.setIcon(it)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(clickListener: OnItemClickListener) =
            TrendingFragment().apply {
                this.clickListener = clickListener
            }
    }
}
