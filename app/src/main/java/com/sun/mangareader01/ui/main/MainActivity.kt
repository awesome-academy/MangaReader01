package com.sun.mangareader01.ui.main

import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.BaseAdapter
import androidx.appcompat.widget.SearchView
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sun.mangareader01.R
import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.source.repository.MangaRepository
import com.sun.mangareader01.ui.adapter.CustomAdapter
import com.sun.mangareader01.ui.adapter.SuggestionAdapter
import com.sun.mangareader01.ui.detail.DetailFragment
import com.sun.mangareader01.ui.home.HomeFragment
import com.sun.mangareader01.ui.mycomics.MyComicsFragment
import com.sun.mangareader01.ui.search.SearchFragment
import com.sun.mangareader01.ui.trending.TrendingFragment
import com.sun.mangareader01.utils.Extensions.showToast
import kotlinx.android.synthetic.main.activity_main.listSuggestions
import kotlinx.android.synthetic.main.activity_main.viewNavigationBar
import kotlinx.android.synthetic.main.activity_main.viewSearch

class MainActivity : FragmentActivity(),
    MainContract.View,
    SearchView.OnQueryTextListener,
    CustomAdapter.OnItemClickListener<Manga>,
    BottomNavigationView.OnNavigationItemSelectedListener {

    private val presenter: MainContract.Presenter by lazy {
        MainPresenter(this, MangaRepository)
    }
    private val searchHandler: Handler by lazy { Handler() }
    private val searchAdapter: CustomAdapter<Manga> by lazy {
        SuggestionAdapter(mutableListOf())
    }
    private var isTypingSearch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initListener()
    }

    private fun initView() {
        replaceFragment(HomeFragment())
        listSuggestions.adapter = searchAdapter as BaseAdapter
    }

    private fun initListener() {
        viewNavigationBar.setOnNavigationItemSelectedListener(this)
        viewSearch.apply {
            setOnClickListener {
                isIconified = false
                isTypingSearch = true
            }
            setOnQueryTextListener(this@MainActivity)
        }
        searchAdapter.onItemClickListener = this
    }

    override fun showSuggestions(mangas: List<Manga>) {
        if (mangas.any() && isTypingSearch) {
            updateSearchAdapter(mangas)
            displaySuggestions()
        } else hideSuggestions()
    }

    override fun showError(exception: Exception) {
        showToast(exception.toString())
    }

    private fun getCurrentFragment() =
        supportFragmentManager.findFragmentById(R.id.layoutContainerFragment)

    private fun isDisplayingSearchFragment() =
        getCurrentFragment() is SearchFragment

    override fun onQueryTextSubmit(query: String): Boolean {
        viewSearch.clearFocus()
        setIsTypingSearch(false)
        beginSearch(query)
        hideSuggestions()
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        setIsTypingSearch(true)
        getSuggestionsLater(newText)
        return true
    }

    // On suggestion item click listener
    override fun onItemClick(item: Manga) = openMangaDetail(item)

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemHomeTab -> HomeFragment()
            R.id.itemTrendingTab -> TrendingFragment()
            R.id.itemMyComicsTab -> MyComicsFragment()
            else -> null
        }?.let { replaceFragment(it) }
        return true
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.layoutContainerFragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.layoutContainerFragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun getSuggestionsLater(keyword: String) {
        searchHandler.apply {
            removeCallbacksAndMessages(null)
            postDelayed(DELAY_TYPING_CHANGE) {
                if (keyword.isBlank()) hideSuggestions()
                else presenter.getSuggestions(keyword)
            }
        }
    }

    private fun updateSearchAdapter(mangas: List<Manga>) {
        searchAdapter.apply {
            updateData(mangas)
            updateValue(viewSearch.query)
        }
    }

    private fun setIsTypingSearch(isTyping: Boolean) {
        isTypingSearch = isTyping
    }

    private fun openMangaDetail(manga: Manga) {
        replaceFragment(DetailFragment.newInstance(manga))
    }

    private fun displaySuggestions() {
        listSuggestions.visibility = View.VISIBLE
    }

    private fun beginSearch(keyword: String) {
        if (isDisplayingSearchFragment()) {
            (getCurrentFragment() as SearchFragment).getMangas(keyword)
        } else addFragment(SearchFragment.newInstance(keyword).apply {
            onMangaClickListener = this@MainActivity
        })
        viewSearch.clearFocus()
    }

    private fun hideSuggestions() {
        listSuggestions.visibility = View.GONE
    }

    companion object {
        const val DELAY_TYPING_CHANGE = 200L
    }
}
