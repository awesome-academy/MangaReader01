package com.sun.mangareader01.ui.read

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.widget.ImageView
import androidx.core.app.ShareCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.mangareader01.R
import com.sun.mangareader01.data.model.Chapter
import com.sun.mangareader01.data.source.local.MangaLocalDataSource
import com.sun.mangareader01.data.source.local.SavePagesCallback
import com.sun.mangareader01.data.source.remote.MangaRemoteDataSource
import com.sun.mangareader01.data.source.remote.SavePagesAsync
import com.sun.mangareader01.data.source.repository.MangaRepository
import com.sun.mangareader01.ui.adapter.PageAdapter
import com.sun.mangareader01.utils.Constants.TYPE_TEXT_PLAIN
import com.sun.mangareader01.utils.Extensions.showToast
import com.sun.mangareader01.utils.FileHelpers
import kotlinx.android.synthetic.main.activity_read.*


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
        MangaRepository.initDataSource(
            MangaRemoteDataSource(),
            MangaLocalDataSource(this)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)
        setContentView(R.layout.activity_read)
        intent?.extras?.also {
            chapter = it.getParcelable(BUNDLE_CHAPTER_KEY)
        }
        initView()
        chapter?.also {
            val chapterDir = FileHelpers.getChapterDir(this, it)
            if (chapterDir.exists()) {
                hideDownloadIcon()
                showPages(FileHelpers.getPageUrls(chapterDir))
            } else {
                presenter.getPages(it)
            }
        }
    }

    override fun onBackPressed() {
        if (imageZoom.isVisible) {
            hideZoomPage()
        } else {
            super.onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imagePage -> displayZoomPage(v as ImageView)
            R.id.imageShareIcon -> shareChapter()
            R.id.imageDownloadIcon -> chapter?.also {
                SavePagesAsync(this, object : SavePagesCallback {
                    override fun onComplete(isSuccessful: Boolean) =
                        confirmSavedChapter()

                    override fun onUpdate(done: Int) =
                        updateSavingChapter(done)
                }).execute(it.pageUrls)
            }
        }
    }

    private fun shareChapter() {
        chapter?.also {
            ShareCompat.IntentBuilder
                .from(this)
                .setType(TYPE_TEXT_PLAIN)
                .setText(it.url)
                .startChooser()
        }
    }

    override fun showPages(pageUrls: List<String>) {
        chapter?.pageUrls?.run {
            clear()
            addAll(pageUrls)
        }
        pageAdapter.updateData(pageUrls)
        updateCurrentPage(FIRST_PAGE_NUMBER, pageAdapter.itemCount)
    }


    override fun showError(exception: Exception) {
        showToast(exception.toString())
    }

    private fun updateSavingChapter(done: Int) {
        val total = chapter?.pageUrls?.size ?: 0
        textDownloadUpdate?.text = getString(
            R.string.text_update_done,
            done, total
        )
    }

    private fun confirmSavedChapter() {
        textDownloadUpdate?.text = getString(R.string.text_saved)
    }

    private fun initView() {
        recyclerChapterPages?.run {
            layoutManager = LinearLayoutManager(this@ReadActivity)
            adapter = pageAdapter
            addOnScrollListener(onScrollListener)
        }
        imageShareIcon?.setOnClickListener(this)
        imageDownloadIcon?.setOnClickListener(this)
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

    private fun hideDownloadIcon() {
        imageDownloadIcon?.visibility = View.INVISIBLE
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
