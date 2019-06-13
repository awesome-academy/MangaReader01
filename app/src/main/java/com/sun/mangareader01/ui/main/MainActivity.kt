package com.sun.mangareader01.ui.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sun.mangareader01.R
import com.sun.mangareader01.ui.home.HomeFragment
import com.sun.mangareader01.ui.mycomics.MyComicsFragment
import com.sun.mangareader01.ui.search.SearchFragment
import com.sun.mangareader01.ui.trending.TrendingFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity(),
    SearchView.OnQueryTextListener,
    AdapterView.OnItemClickListener,
    BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initListener()
    }

    private fun initView() {
        replaceFragment(HomeFragment())
    }

    private fun initListener() {
        viewNavigationBar.setOnNavigationItemSelectedListener(this)
        viewSearch.apply {
            setOnClickListener { isIconified = false }
            setOnQueryTextListener(this@MainActivity)
        }
        listSuggestions.onItemClickListener = this
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.layoutContainerFragment,
            fragment
        ).addToBackStack(null).commit()
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        replaceFragment(SearchFragment.newInstance(query))
        hideSuggestions()
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        displaySuggestions()
        return true
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
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

    private fun displaySuggestions() {
        listSuggestions.visibility = View.VISIBLE
    }

    private fun hideSuggestions() {
        listSuggestions.visibility = View.INVISIBLE
    }
}
