package com.sun.mangareader01.ui.read

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.mangareader01.R
import com.sun.mangareader01.data.model.Chapter
import com.sun.mangareader01.data.source.remote.MangaRemoteDataSource
import com.sun.mangareader01.data.source.repository.MangaRepository
import com.sun.mangareader01.ui.adapter.PageAdapter
import com.sun.mangareader01.utils.Extensions.showToast
import kotlinx.android.synthetic.main.activity_read.imageZoom
import kotlinx.android.synthetic.main.activity_read.recyclerChapterPages
import kotlinx.android.synthetic.main.activity_read.textCurrentPageNumber


class ReadActivity : Activity(),
    ReadContract.View,
    View.OnClickListener {

    private val presenter: ReadContract.Presenter by lazy {
        ReadPresenter(this, MangaRepository)
    }
    private var chapter: Chapter? = null
    private val pageAdapter: PageAdapter by lazy {
        PageAdapter(mutableListOf(), this)
    }
    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(
            recyclerView: RecyclerView,
            dx: Int, dy: Int
        ) {
            super.onScrolled(recyclerView, dx, dy)
            val currentPageNumber =
                (recyclerView.layoutManager as LinearLayoutManager)
                    .findLastCompletelyVisibleItemPosition() + 1
            if (currentPageNumber > 0) {
                updateCurrentPage(
                    currentPageNumber,
                    recyclerView.adapter?.itemCount
                )
            }
        }
    }

    init {
        MangaRepository.initDataSource(MangaRemoteDataSource())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)
        setContentView(R.layout.activity_read)
        chapter = intent?.extras?.getParcelable(BUNDLE_CHAPTER_KEY)
        initView()
        chapter?.also { presenter.getPages(it) }
    }

    override fun onBackPressed() {
        if (imageZoom.isVisible) {
            hideZoomPage()
        } else {
            super.onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.imagePage) displayZoomPage(v as ImageView)
    }

    override fun showPages(pageUrls: List<String>) {
        pageAdapter.updateData(pageUrls)
        updateCurrentPage(FIRST_PAGE_NUMBER, pageAdapter.itemCount)
    }

    override fun showError(exception: Exception) {
        showToast(exception.toString())
    }

    private fun initView() {
        recyclerChapterPages?.run {
            layoutManager = LinearLayoutManager(this@ReadActivity)
            adapter = pageAdapter
            addOnScrollListener(onScrollListener)
        }
    }

    private fun updateCurrentPage(
        currentPageNumber: Int, totalPagesNumber: Int?
    ) {
        textCurrentPageNumber?.text = getString(
            R.string.text_current_page_number,
            currentPageNumber, totalPagesNumber ?: 0
        )
    }

    private fun displayZoomPage(zoomedImage: ImageView) = imageZoom.apply {
        setImageDrawable(zoomedImage.drawable)
        visibility = View.VISIBLE
    }

    private fun hideZoomPage() {
        imageZoom?.visibility = View.INVISIBLE
        imageZoom?.setImageDrawable(null)
    }

    companion object {
        const val BUNDLE_CHAPTER_KEY = "chapter"
        private const val FIRST_PAGE_NUMBER = 1

        fun getProfileIntent(context: Context, chapter: Chapter) =
            Intent(context, ReadActivity::class.java).apply {
                putExtra(BUNDLE_CHAPTER_KEY, chapter)
            }
    }
}
