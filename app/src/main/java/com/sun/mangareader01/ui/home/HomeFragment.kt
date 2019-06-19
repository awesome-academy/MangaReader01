package com.sun.mangareader01.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sun.mangareader01.R
import com.sun.mangareader01.ui.adapter.CategoryAdapter
import com.sun.mangareader01.ui.listener.OnItemClickListener

class HomeFragment : Fragment() {

    private var onItemClickListener: OnItemClickListener? = null

    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter(mutableListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            onItemClickListener = it.getParcelable(BUNDLE_CLICK_LISTENER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryAdapter.onItemClickListener = onItemClickListener
    }

    companion object {
        private const val BUNDLE_CLICK_LISTENER = "clickListener"

        @JvmStatic
        fun newInstance(clickListener: OnItemClickListener) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_CLICK_LISTENER, clickListener)
                }
            }
    }
}
