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
import com.sun.mangareader01.ui.adapter.ActionMangaAdapter
import com.sun.mangareader01.ui.adapter.CustomAdapter
import com.sun.mangareader01.ui.listener.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_my_comics.barLoading
import kotlinx.android.synthetic.main.fragment_my_comics.recyclerMyComics

class MyComicsFragment : Fragment() {

    private var clickListener: OnItemClickListener? = null
    private val mangaAdapter: CustomAdapter<Manga> by lazy {
        ActionMangaAdapter(mutableListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            clickListener = it.getParcelable(BUNDLE_CLICK_LISTENER_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_my_comics, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mangaAdapter.onItemClickListener = clickListener
        recyclerMyComics.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mangaAdapter as RecyclerView.Adapter<*>
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(context, VERTICAL))
        }
    }

    private fun showLoadingBar() {
        barLoading.visibility = View.VISIBLE
    }

    private fun hideLoadingBar() {
        barLoading.visibility = View.GONE
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
