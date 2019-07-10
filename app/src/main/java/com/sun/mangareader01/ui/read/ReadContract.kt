package com.sun.mangareader01.ui.read

import com.sun.mangareader01.data.model.Chapter

interface ReadContract {
    interface View {
        fun showPages(pageUrls: List<String>)
        fun showError(exception: Exception)
        fun confirmSavedChapter()
        fun updateSavingChapter(done: Int)
    }
    interface Presenter {
        fun getPages(chapter: Chapter)
    }
}
