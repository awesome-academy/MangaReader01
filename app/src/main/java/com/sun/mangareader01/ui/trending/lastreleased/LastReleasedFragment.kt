package com.sun.mangareader01.ui.trending.lastreleased

import android.os.Bundle
import com.sun.mangareader01.R
import com.sun.mangareader01.data.source.repository.MangaRepository
import com.sun.mangareader01.ui.listener.OnItemClickListener
import com.sun.mangareader01.ui.trending.pager.PagerContract
import com.sun.mangareader01.ui.trending.pager.PagerFragment

class LastReleasedFragment : PagerFragment() {

    override val title = TITLE_LAST_RELEASED
    override val icon = R.drawable.ic_new_releases_black_24dp
    override val presenter: PagerContract.Presenter by lazy {
        LastReleasedPresenter(this, MangaRepository)
    }

    companion object {
        private const val TITLE_LAST_RELEASED = "Last release"

        @JvmStatic
        fun newInstance(clickListener: OnItemClickListener?) =
            LastReleasedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_CLICK_LISTENER_KEY, clickListener)
                }
            }
    }
}
