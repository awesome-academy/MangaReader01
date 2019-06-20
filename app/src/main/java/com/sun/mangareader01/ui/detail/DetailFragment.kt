package com.sun.mangareader01.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sun.mangareader01.R
import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.model.MangaDetail
import com.sun.mangareader01.data.source.repository.MangaRepository
import com.sun.mangareader01.utils.Extensions.setImageUrl
import com.sun.mangareader01.utils.Extensions.showToast
import com.sun.mangareader01.utils.Helpers.buildCoverUrl
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment(), DetailContract.View {

    private val presenter: DetailContract.Presenter by lazy {
        DetailPresenter(this, MangaRepository)
    }
    private lateinit var manga: Manga

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
        displayLoading()
        presenter.getDetail(manga)
    }

    override fun showMangaDetail(data: MangaDetail) {
        hideLoading()
        textMangaTitle.text = data.title
        imageBackComicCover.setImageUrl(buildCoverUrl(manga.slug))
        imageComicCover.setImageUrl(buildCoverUrl(manga.slug))
        textRating.text = getString(R.string.text_rating, data.rating)
        barRating.rating = data.rating
        if(data.author.isNotBlank()) textAuthor.text = data.author
        textSummary.text = data.summary
    }

    override fun showError(exception: Exception) {
        context?.showToast(exception.toString())
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

        @JvmStatic
        fun newInstance(manga: Manga?) = DetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(BUNDLE_MANGA_KEY, manga)
            }
        }
    }
}
