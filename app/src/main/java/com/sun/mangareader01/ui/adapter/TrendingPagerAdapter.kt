package com.sun.mangareader01.ui.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.sun.mangareader01.ui.trending.pager.PagerFragment

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
}
