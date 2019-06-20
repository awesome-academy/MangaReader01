package com.sun.mangareader01.ui.read

import com.sun.mangareader01.data.model.Chapter

interface ReadContract {
    interface View {
        fun showPages(pageUrls: List<String>)
        fun showError(exception: Exception)
    }
    interface Presenter {
        fun getPages(chapter: Chapter)
    }
}
