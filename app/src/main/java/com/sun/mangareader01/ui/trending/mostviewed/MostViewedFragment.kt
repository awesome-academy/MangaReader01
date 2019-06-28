package com.sun.mangareader01.ui.trending.mostviewed

import android.os.Bundle
import com.sun.mangareader01.R
import com.sun.mangareader01.data.source.repository.MangaRepository
import com.sun.mangareader01.ui.listener.OnItemClickListener
import com.sun.mangareader01.ui.trending.pager.PagerContract
import com.sun.mangareader01.ui.trending.pager.PagerFragment

class MostViewedFragment : PagerFragment() {

    override val title = TITLE_MOST_VIEWED
    override val icon = R.drawable.ic_grade_black_24dp
    override val presenter: PagerContract.Presenter by lazy {
        MostViewedPresenter(this, MangaRepository)
    }

    companion object {
        private const val TITLE_MOST_VIEWED = "Most viewed"

        @JvmStatic
        fun newInstance(clickListener: OnItemClickListener?) =
            MostViewedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_CLICK_LISTENER_KEY, clickListener)
                }
            }
    }
}
