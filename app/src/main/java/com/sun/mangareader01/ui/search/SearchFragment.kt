package com.sun.mangareader01.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sun.mangareader01.R
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment() {

    private lateinit var keyword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            keyword = it.getString(BUNDLE_SEARCH_KEY) ?: ""
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_search, container, false).apply {
            textSearchResult.text = keyword
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
