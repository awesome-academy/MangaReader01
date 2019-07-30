package com.sun.mangareader01.ui.search

import com.nhaarman.mockitokotlin2.capture
import com.nhaarman.mockitokotlin2.eq
import com.sun.mangareader01.data.model.MangasResponse
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback
import com.sun.mangareader01.data.source.repository.MangaRepository
import org.junit.Before
import org.junit.Test
import org.mockito.*

class SearchPresenterTest {

    @Mock
    private lateinit var repository: MangaRepository
    @Mock
    private lateinit var view: SearchContract.View
    @Mock
    private lateinit var response: MangasResponse
    @Mock
    private lateinit var exception: Exception

    private lateinit var presenter: SearchContract.Presenter

    @Captor
    private lateinit var loadDataCallbackCaptor:
        ArgumentCaptor<OnLoadedDataCallback<MangasResponse>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = SearchPresenter(view, repository)
    }

    @Test
    fun getMangasSuccessfulTest() {
        presenter.getMangas(query)
        Mockito.verify(repository).getMangas(
            eq(query),
            capture(loadDataCallbackCaptor)
        )
        loadDataCallbackCaptor.value.onSuccessful(response)
        Mockito.verify(view).showMangas(response.mangas)
    }

    @Test
    fun getMangasFailedTest() {
        presenter.getMangas(query)
        Mockito.verify(repository).getMangas(
            eq(query),
            capture(loadDataCallbackCaptor)
        )
        loadDataCallbackCaptor.value.onFailed(exception)
        Mockito.verify(view).showError(exception)
    }

    companion object {
        private const val query = "query"
    }
}
