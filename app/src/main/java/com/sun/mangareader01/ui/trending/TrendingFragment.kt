package com.sun.mangareader01.ui.trending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.sun.mangareader01.R
import com.sun.mangareader01.ui.adapter.TrendingPagerAdapter
import com.sun.mangareader01.ui.listener.OnItemClickListener
import com.sun.mangareader01.ui.trending.hot.HotFragment
import com.sun.mangareader01.ui.trending.lastreleased.LastReleasedFragment
import com.sun.mangareader01.ui.trending.mostviewed.MostViewedFragment
import com.sun.mangareader01.ui.trending.pager.PagerFragment
import kotlinx.android.synthetic.main.fragment_trending.pagerTrending
import kotlinx.android.synthetic.main.fragment_trending.tabTrending

class TrendingFragment : Fragment(),
    TabLayout.OnTabSelectedListener {

    private var clickListener: OnItemClickListener? = null
    private val hotFragment: PagerFragment by lazy {
        HotFragment.newInstance(clickListener)
    }
    private val mostViewedFragment: PagerFragment by lazy {
        MostViewedFragment.newInstance(clickListener)
    }
    private val lastReleaseFragment: PagerFragment by lazy {
        LastReleasedFragment.newInstance(clickListener)
    }
    private val pagerFragments: List<PagerFragment> by lazy {
        listOf(hotFragment, mostViewedFragment, lastReleaseFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            this.clickListener = it.getParcelable(BUNDLE_CLICK_LISTENER_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trending, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabPager()
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
        pagerFragments[tab.position].onRefresh()
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
        private const val BUNDLE_CLICK_LISTENER_KEY = "clickListener"

        @JvmStatic
        fun newInstance(clickListener: OnItemClickListener) =
            TrendingFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_CLICK_LISTENER_KEY, clickListener)
                }
            }
    }
}
