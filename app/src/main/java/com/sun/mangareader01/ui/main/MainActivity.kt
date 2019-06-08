package com.sun.mangareader01.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.sun.mangareader01.R
import com.sun.mangareader01.ui.home.HomeFragment
import com.sun.mangareader01.ui.mycomics.MyComicsFragment
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
        viewSearch.setOnClickListener {
            viewSearch.isIconified = false
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.layoutContainerFragment,
            fragment
        ).commit()
    }
}
