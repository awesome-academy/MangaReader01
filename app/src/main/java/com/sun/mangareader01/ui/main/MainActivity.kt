package com.sun.mangareader01.ui.main

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sun.mangareader01.R
import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.source.repository.MangaRepository
import com.sun.mangareader01.ui.adapter.CustomBaseAdapter
import com.sun.mangareader01.ui.adapter.SuggestionAdapter
import com.sun.mangareader01.ui.home.HomeFragment
import com.sun.mangareader01.ui.mycomics.MyComicsFragment
import com.sun.mangareader01.ui.trending.TrendingFragment
import com.sun.mangareader01.utils.Extensions.showToast
import kotlinx.android.synthetic.main.activity_main.listSuggestions
import kotlinx.android.synthetic.main.activity_main.viewNavigationBar
import kotlinx.android.synthetic.main.activity_main.viewSearch

const val DELAY_TYPING_CHANGE = 350L

class MainActivity : FragmentActivity(),
    MainContract.View,
    SearchView.OnQueryTextListener,
    CustomBaseAdapter.ItemClickListener<Manga>,
    BottomNavigationView.OnNavigationItemSelectedListener {

    private var presenter: MainContract.Presenter? = null
    private val searchHandler: Handler by lazy { Handler() }
    private val searchAdapter: CustomBaseAdapter<Manga> by lazy {
        SuggestionAdapter(mutableListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initPresenter()
        initView()
        initListener()
    }

    private fun initView() {
        replaceFragment(HomeFragment())
        listSuggestions.adapter = searchAdapter
    }

    private fun initListener() {
        viewNavigationBar.setOnNavigationItemSelectedListener(this)
        viewSearch.apply {
            setOnClickListener { isIconified = false }
            setOnQueryTextListener(this@MainActivity)
        }
        searchAdapter.setOnItemClickListener(this)
    }

    private fun initPresenter() {
        setPresenter(MainPresenter(this))
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    override fun showSuggestions(mangas: List<Manga>) {
        if (mangas.any()) {
            displaySuggestions()
            updateSearchAdapter(mangas)
        } else hideSuggestions()
    }

    override fun showError(exception: Exception) {
        showToast(exception.toString())
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        getSuggestionsLater(newText)
        return true
    }

    // On suggestion item click listener
    override fun onItemClick(item: Manga) {
        Log.e("Main", "${item.title}")
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemHomeTab -> HomeFragment()
            R.id.itemTrendingTab -> TrendingFragment()
            R.id.itemMyComicsTab -> MyComicsFragment()
            else -> null
        }?.let { replaceFragment(it) }
        return true
    }

    private fun addFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction()
            .add(R.id.layoutContainerFragment, fragment)
            .addToBackStack(null)
            .commit()

    private fun replaceFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction()
            .replace(R.id.layoutContainerFragment, fragment)
            .addToBackStack(null)
            .commit()

    private fun getSuggestionsLater(keyword: String) {
        searchHandler.apply {
            removeCallbacksAndMessages(null)
            postDelayed(DELAY_TYPING_CHANGE) {
                if (keyword.isBlank()) hideSuggestions()
                else presenter?.getSuggestions(keyword)
            }
        }
    }

    private fun updateSearchAdapter(mangas: List<Manga>) {
        searchAdapter.apply {
            updateData(mangas)
            updateValue(viewSearch.query)
        }
    }

    private fun displaySuggestions() {
        listSuggestions.visibility = View.VISIBLE
    }

    private fun hideSuggestions() {
        listSuggestions.visibility = View.GONE
    }
}
