package com.sun.mangareader01.ui.read

import com.nhaarman.mockitokotlin2.capture
import com.nhaarman.mockitokotlin2.eq
import com.sun.mangareader01.data.model.Chapter
import com.sun.mangareader01.data.model.PagesResponse
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback
import com.sun.mangareader01.data.source.remote.MangaRemoteDataSource
import com.sun.mangareader01.data.source.repository.MangaRepository
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class ReadPresenterTest {

    @Mock
    private lateinit var repository: MangaRepository
    @Mock
    private lateinit var view: ReadContract.View
    @Mock
    private lateinit var chapter: Chapter
    @Mock
    private lateinit var response: PagesResponse
    @Mock
    private lateinit var exception: Exception

    private lateinit var presenter: ReadContract.Presenter

    @Captor
    private lateinit var loadDataCallbackCaptor:
        ArgumentCaptor<OnLoadedDataCallback<PagesResponse>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository.initDataSource(MangaRemoteDataSource())
        presenter = ReadPresenter(view, repository)

    }

    @Test
    fun getPagesSuccessfulTest() {
        presenter.getPages(chapter)
        verify(repository).getPages(
            eq(chapter),
            capture(loadDataCallbackCaptor)
        )
        loadDataCallbackCaptor.value.onSuccessful(response)
        verify(view).showPages(response.pageUrls)
    }

    @Test
    fun getPagesFailedTest() {
        presenter.getPages(chapter)
        verify(repository).getPages(
            eq(chapter),
            capture(loadDataCallbackCaptor)
        )
        loadDataCallbackCaptor.value.onFailed(exception)
        verify(view).showError(exception)
    }
}
