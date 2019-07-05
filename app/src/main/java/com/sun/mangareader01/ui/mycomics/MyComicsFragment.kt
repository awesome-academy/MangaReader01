package com.sun.mangareader01.ui.mycomics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.mangareader01.R
import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.source.repository.MangaRepository
import com.sun.mangareader01.ui.adapter.ActionMangaAdapter
import com.sun.mangareader01.ui.adapter.MangaAdapter
import com.sun.mangareader01.ui.listener.OnItemClickListener
import com.sun.mangareader01.ui.listener.OnMangaActionListener
import com.sun.mangareader01.utils.Extensions.showToast
import kotlinx.android.synthetic.main.fragment_my_comics.barLoading
import kotlinx.android.synthetic.main.fragment_my_comics.recyclerMyComics

class MyComicsFragment : Fragment(),
    MyComicsContract.View {

    private val presenter: MyComicsContract.Presenter by lazy {
        MyComicsPresenter(this, MangaRepository)
    }
    private val mangas: MutableList<Manga> by lazy {
        mutableListOf<Manga>()
    }
    private val mangaAdapter: ActionMangaAdapter by lazy {
        MangaAdapter(mangas)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            clickListener = it.getParcelable(BUNDLE_CLICK_LISTENER_KEY)
        }
    }

    private var clickListener: OnItemClickListener? = null
    private var onMangaActionListener = object : OnMangaActionListener {
        override fun onDeleteManga(manga: Manga?) {
            manga?.also {
                val position = mangas.indexOf(it)
                mangas.remove(it)
                (mangaAdapter as MangaAdapter).apply {
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, mangas.size)
                }
                presenter.deleteManga(it)
            }
        }

        override fun onDownloadManga(manga: Manga?) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_my_comics, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mangaAdapter.run {
            layoutViewResourceId = R.layout.item_manga_action
            onItemClickListener = clickListener
            onMangaActionListener = this@MyComicsFragment.onMangaActionListener
        }
        recyclerMyComics?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = mangaAdapter as RecyclerView.Adapter<*>
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(context, VERTICAL))
        }
        showLoadingBar()
        presenter.getMyMangas()
    }

    override fun showMangas(mangas: List<Manga>) {
        mangaAdapter.updateData(mangas)
        hideLoadingBar()
    }

    override fun showError(exception: Exception) {
        context?.showToast(exception.toString())
        hideLoadingBar()
    }

    override fun confirmDeleted(successful: Boolean) {
        if (!successful) presenter.getMyMangas()
    }

    private fun showLoadingBar() {
        barLoading?.visibility = View.VISIBLE
    }

    private fun hideLoadingBar() {
        barLoading?.visibility = View.GONE
    }

    companion object {

        private const val BUNDLE_CLICK_LISTENER_KEY = "clickListener"

        fun newInstance(clickListener: OnItemClickListener) =
            MyComicsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_CLICK_LISTENER_KEY, clickListener)
                }
            }
    }
}
