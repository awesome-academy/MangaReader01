package com.sun.mangareader01.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Parcel
import android.view.MenuItem
import android.view.View
import android.widget.BaseAdapter
import androidx.appcompat.widget.SearchView
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sun.mangareader01.R
import com.sun.mangareader01.data.model.Chapter
import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.source.remote.MangaRemoteDataSource
import com.sun.mangareader01.data.source.repository.MangaRepository
import com.sun.mangareader01.ui.adapter.CustomAdapter
import com.sun.mangareader01.ui.adapter.SuggestionAdapter
import com.sun.mangareader01.ui.detail.DetailFragment
import com.sun.mangareader01.ui.home.HomeFragment
import com.sun.mangareader01.ui.listener.OnItemClickListener
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
    BottomNavigationView.OnNavigationItemSelectedListener {

    init {
        MangaRepository.initDataSource(MangaRemoteDataSource())
    }

    private val presenter: MainContract.Presenter by lazy {
        MainPresenter(this, MangaRepository)
    }
    private val searchHandler: Handler by lazy { Handler() }
    private val searchAdapter: CustomAdapter<Manga> by lazy {
        SuggestionAdapter(mutableListOf())
    }
    private var isTypingSearch = false
    private val trendingFragment: TrendingFragment by lazy {
        TrendingFragment.newInstance(onItemClickListener)
    }

    private val onItemClickListener = object : OnItemClickListener {
        override fun onMangaClick(manga: Manga?) {
            manga?.let { openMangaDetail(it) }
        }

        override fun onChapterClick(chapter: Chapter?) {
        }

        override fun onTagClick(tag: String?) {
            tag?.let { viewSearch.setQuery(it, true) }
        }

        override fun writeToParcel(dest: Parcel?, flags: Int) {
        }

        override fun describeContents(): Int = 0
    }

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
        searchAdapter.onItemClickListener = onItemClickListener
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
        setIsTypingSearch(false)
        beginSearch(query)
        hideSuggestions()
        viewSearch.clearFocus()
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        setIsTypingSearch(true)
        getSuggestionsLater(newText)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemHomeTab -> HomeFragment()
            R.id.itemTrendingTab -> trendingFragment
            R.id.itemMyComicsTab -> MyComicsFragment()
            else -> null
        }?.also { replaceFragment(it) }
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
        viewSearch.clearFocus()
        hideSuggestions()
        replaceFragment(DetailFragment.newInstance(manga, onItemClickListener))
    }

    private fun displaySuggestions() {
        listSuggestions.visibility = View.VISIBLE
    }

    private fun beginSearch(keyword: String) {
        if (isDisplayingSearchFragment())
            (getCurrentFragment() as SearchFragment).getMangas(keyword)
        else addFragment(
            SearchFragment.newInstance(keyword, onItemClickListener)
        )
        viewSearch.clearFocus()
    }

    private fun hideSuggestions() {
        listSuggestions.visibility = View.GONE
    }

    companion object {
        const val DELAY_TYPING_CHANGE = 200L
    }
}
