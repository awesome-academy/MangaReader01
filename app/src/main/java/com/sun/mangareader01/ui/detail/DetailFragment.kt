package com.sun.mangareader01.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexDirection.ROW
import com.google.android.flexbox.FlexboxLayoutManager
import com.sun.mangareader01.R
import com.sun.mangareader01.data.model.Chapter
import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.model.MangaDetail
import com.sun.mangareader01.data.source.remote.MangaRemoteDataSource
import com.sun.mangareader01.data.source.repository.MangaRepository
import com.sun.mangareader01.ui.adapter.ChapterAdapter
import com.sun.mangareader01.ui.adapter.TagAdapter
import com.sun.mangareader01.utils.Extensions.setImageUrl
import com.sun.mangareader01.utils.Extensions.showToast
import com.sun.mangareader01.utils.Helpers.buildCoverUrl
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment(), DetailContract.View {

    private val presenter: DetailContract.Presenter by lazy {
        DetailPresenter(this, MangaRepository)
    }
    private lateinit var manga: Manga
    private val chapterAdapter: ChapterAdapter by lazy {
        ChapterAdapter(mutableListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            manga = it.getParcelable(BUNDLE_MANGA_KEY) as Manga
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showExistDetails()
        displayLoading()
        presenter.getDetail(manga)
    }

    override fun showMangaDetail(data: MangaDetail) {
        hideLoading()
        showRating(data.rating)
        if (data.author.isNotBlank()) textAuthor.text = data.author
        textSummary.text = data.summary
        showTags(data.tags)
        showChapters(data.chapters)
    }

    override fun showError(exception: Exception) {
        context?.showToast(exception.toString())
    }

    private fun showExistDetails() {
        textMangaTitle.text = manga.title
        imageBackComicCover.setImageUrl(buildCoverUrl(manga.slug))
        imageComicCover.setImageUrl(buildCoverUrl(manga.slug))
    }

    private fun showRating(rating: Float) {
        barRating.rating = rating
        textRating.text = getString(R.string.text_rating, rating)
    }

    private fun showTags(tags: List<String>) {
        recyclerTags.apply {
            layoutManager = FlexboxLayoutManager(context, ROW)
            adapter = TagAdapter(tags)
        }
    }

    private fun showChapters(chapters: List<Chapter>) {
        recyclerChapters.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chapterAdapter
        }
        chapterAdapter.updateData(chapters.take(LIMIT_CHAPTER_SHOW))
        showLoadMore(chapters)
    }

    private fun showLoadMore(chapters: List<Chapter>) {
        if (chapters.size <= chapterAdapter.itemCount) hideLoadMore()
        else textLoadMore.setOnClickListener {
            chapterAdapter.updateData(
                chapters.take(chapterAdapter.itemCount + LIMIT_CHAPTER_SHOW)
            )
            if (chapterAdapter.itemCount == chapters.size) hideLoadMore()
        }
    }

    private fun hideLoadMore() {
        imageLoadMoreIcon.visibility = View.GONE
        textLoadMore.visibility = View.GONE
    }

    private fun displayLoading() {
        viewLoading.visibility = View.VISIBLE
        barLoading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        viewLoading.visibility = View.GONE
        barLoading.visibility = View.GONE
    }

    companion object {
        private const val BUNDLE_MANGA_KEY = "manga"
        private const val LIMIT_CHAPTER_SHOW = 5

        @JvmStatic
        fun newInstance(manga: Manga?) = DetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(BUNDLE_MANGA_KEY, manga)
            }
        }
    }
}
