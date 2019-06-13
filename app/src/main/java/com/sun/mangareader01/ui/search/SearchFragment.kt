package com.sun.mangareader01.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sun.mangareader01.R
import kotlinx.android.synthetic.main.fragment_search.recyclerSearchResult

class SearchFragment : Fragment(), SearchContract.View {

    private lateinit var keyword: String
    private lateinit var searchPresenter: SearchContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            keyword = it.getString(BUNDLE_SEARCH_KEY) ?: ""
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerSearchResult.layoutManager = LinearLayoutManager(context)
    }
    override fun setPresenter(presenter: SearchContract.Presenter) {
        searchPresenter = presenter
    }

    companion object {
        private const val BUNDLE_SEARCH_KEY = "keyword"

        @JvmStatic
        fun newInstance(keyword: String?) = SearchFragment().apply {
            arguments = Bundle().apply {
                putString(BUNDLE_SEARCH_KEY, keyword)
            }
        }
    }
}
