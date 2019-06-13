package com.sun.mangareader01.ui.search

interface SearchContract {
    interface View {
        fun setPresenter(presenter: Presenter)
    }

    interface Presenter {
        fun getSearchResults(query: String)
    }
}
