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
import com.sun.mangareader01.data.source.repository.MangaRepository
import com.sun.mangareader01.ui.adapter.ChapterAdapter
import com.sun.mangareader01.ui.adapter.TagAdapter
import com.sun.mangareader01.ui.listener.OnItemClickListener
import com.sun.mangareader01.utils.Constants.EMPTY_STRING
import com.sun.mangareader01.utils.Extensions.setImageUrl
import com.sun.mangareader01.utils.Extensions.showToast
import com.sun.mangareader01.utils.Helpers.buildCoverUrl
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment(),
    DetailContract.View {

    private val presenter: DetailContract.Presenter by lazy {
        DetailPresenter(this, MangaRepository)
    }
    private var manga: Manga = Manga(EMPTY_STRING, EMPTY_STRING)
    private val tagAdapter: TagAdapter by lazy { TagAdapter(mutableListOf()) }
    private val chapterAdapter: ChapterAdapter by lazy {
        ChapterAdapter(mutableListOf())
    }
    private var clickListener: OnItemClickListener? = null

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
    ): View? = inflater.inflate(R.layout.fragment_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showExistDetails()
        displayLoading()
        tagAdapter.onItemClickListener = clickListener
        chapterAdapter.onItemClickListener = clickListener
        presenter.getDetail(manga)
    }

    override fun showMangaDetail(data: MangaDetail) {
        hideLoading()
        showRating(data.rating)
        if (data.author.isNotBlank()) textAuthor?.text = data.author
        textSummary?.text = data.summary
        showTags(data.tags)
        showChapters(data.chapters)
    }

    override fun showError(exception: Exception) {
        hideLoading()
        context?.showToast(exception.toString())
    }

    private fun showExistDetails() {
        textMangaTitle?.text = manga.title
        imageBackComicCover?.setImageUrl(
            url = buildCoverUrl(manga.slug),
            blurred = true
        )
        imageComicCover?.setImageUrl(buildCoverUrl(manga.slug))
    }

    private fun showRating(rating: Float) {
        barRating?.rating = rating
        textRating?.text = getString(R.string.text_rating, rating)
    }

    private fun showTags(tags: List<String>) {
        recyclerTags?.apply {
            layoutManager = FlexboxLayoutManager(context, ROW)
            adapter = tagAdapter.also { it.updateData(tags) }
        }
    }

    private fun showChapters(allChapters: List<Chapter>) {
        recyclerChapters?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chapterAdapter
        }
        showMoreChapters(allChapters)
        textLoadMore?.setOnClickListener {
            showMoreChapters(allChapters)
        }
    }

    private fun showMoreChapters(allChapters: List<Chapter>) {
        val newNumberChapters = chapterAdapter.itemCount + LIMIT_CHAPTER_SHOW
        chapterAdapter.updateData(allChapters.take(newNumberChapters))
        if (hasMoreChapters(allChapters)) showLoadMore()
        else hideLoadMore()
    }

    private fun hasMoreChapters(chapters: List<Chapter>) =
        chapterAdapter.itemCount < chapters.size

    private fun showLoadMore() {
        imageLoadMoreIcon?.visibility = View.VISIBLE
        textLoadMore?.visibility = View.VISIBLE
    }

    private fun hideLoadMore() {
        imageLoadMoreIcon?.visibility = View.GONE
        textLoadMore?.visibility = View.GONE
    }

    private fun displayLoading() {
        viewLoading?.visibility = View.VISIBLE
        barLoading?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        viewLoading?.visibility = View.GONE
        barLoading?.visibility = View.GONE
    }

    companion object {
        private const val BUNDLE_MANGA_KEY = "manga"
        private const val LIMIT_CHAPTER_SHOW = 5

        @JvmStatic
        fun newInstance(manga: Manga?, clickListener: OnItemClickListener) =
            DetailFragment().apply {
                this.clickListener = clickListener
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_MANGA_KEY, manga)
                }
            }
    }
}
