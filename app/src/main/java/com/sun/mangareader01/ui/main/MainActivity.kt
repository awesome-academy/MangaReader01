package com.sun.mangareader01.ui.main

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.sun.mangareader01.R
import com.sun.mangareader01.ui.home.HomeFragment
import com.sun.mangareader01.ui.mycomics.MyComicsFragment
import com.sun.mangareader01.ui.search.SearchFragment
import com.sun.mangareader01.ui.trending.TrendingFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity() {

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
        viewNavigationBar.setOnNavigationItemSelectedListener { navigationItem ->
            when (navigationItem.itemId) {
                R.id.itemHomeTab -> HomeFragment()
                R.id.itemTrendingTab -> TrendingFragment()
                R.id.itemMyComicsTab -> MyComicsFragment()
                else -> null
            }?.let { replaceFragment(it) }
            true
        }
        viewSearch.apply {
            setOnClickListener { isIconified = false }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    addFragment(SearchFragment.newInstance(query))
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // Quick search response here
                    return true
                }

            })
        }
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(
            R.id.layoutContainerFragment,
            fragment
        ).addToBackStack(null).commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.layoutContainerFragment,
            fragment
        ).addToBackStack(null).commit()
    }
}
