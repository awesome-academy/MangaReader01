package com.sun.mangareader01.ui.trending.mostviewed

import android.os.Bundle
import com.sun.mangareader01.R
import com.sun.mangareader01.ui.listener.OnItemClickListener
import com.sun.mangareader01.ui.trending.pager.PagerFragment

class MostViewedFragment : PagerFragment() {

    override val title: String? = getString(R.string.title_most_viewed)
    override val icon: Int? = R.drawable.ic_grade_black_24dp

    companion object {
        @JvmStatic
        fun newInstance(clickListener: OnItemClickListener?) =
            MostViewedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_CLICK_LISTENER_KEY, clickListener)
                }
            }
    }
}
