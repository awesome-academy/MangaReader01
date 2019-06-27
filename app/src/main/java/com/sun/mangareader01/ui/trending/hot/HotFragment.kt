package com.sun.mangareader01.ui.trending.hot

import android.os.Bundle
import com.sun.mangareader01.R
import com.sun.mangareader01.ui.listener.OnItemClickListener
import com.sun.mangareader01.ui.trending.pager.PagerFragment

class HotFragment : PagerFragment() {

    override val title: String? = getString(R.string.title_hot)
    override val icon: Int? = R.drawable.ic_whatshot_black_24dp

    companion object {
        @JvmStatic
        fun newInstance(clickListener: OnItemClickListener?) =
            HotFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_CLICK_LISTENER_KEY, clickListener)
                }
            }
    }
}
