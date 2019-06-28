package com.sun.mangareader01.ui.trending.lastreleased

import android.os.Bundle
import com.sun.mangareader01.R
import com.sun.mangareader01.ui.listener.OnItemClickListener
import com.sun.mangareader01.ui.trending.pager.PagerFragment

class LastReleasedFragment : PagerFragment() {

    override val title: String? = getString(R.string.title_last_release)
    override val icon: Int? = R.drawable.ic_new_releases_black_24dp

    companion object {
        @JvmStatic
        fun newInstance(clickListener: OnItemClickListener?) =
            LastReleasedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_CLICK_LISTENER_KEY, clickListener)
                }
            }
    }
}
