package com.sun.mangareader01.ui.trending.hot

import android.os.Bundle
import com.sun.mangareader01.R
import com.sun.mangareader01.data.source.repository.MangaRepository
import com.sun.mangareader01.ui.listener.OnItemClickListener
import com.sun.mangareader01.ui.trending.pager.PagerContract
import com.sun.mangareader01.ui.trending.pager.PagerFragment

class HotFragment : PagerFragment() {

    override val title = TITLE_HOT
    override val icon = R.drawable.ic_whatshot_black_24dp
    override val presenter: PagerContract.Presenter by lazy {
        HotPresenter(this, MangaRepository)
    }

    companion object {
        private const val TITLE_HOT = "Hot updates"

        @JvmStatic
        fun newInstance(clickListener: OnItemClickListener?) =
            HotFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_CLICK_LISTENER_KEY, clickListener)
                }
            }
    }
}
