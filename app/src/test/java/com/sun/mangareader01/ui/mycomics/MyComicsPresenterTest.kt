package com.sun.mangareader01.ui.mycomics

import com.nhaarman.mockitokotlin2.capture
import com.nhaarman.mockitokotlin2.eq
import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.model.MangasResponse
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback
import com.sun.mangareader01.data.source.repository.MangaRepository
import org.junit.Before
import org.junit.Test
import org.mockito.*

class MyComicsPresenterTest {

    @Mock
    private lateinit var repository: MangaRepository
    @Mock
    private lateinit var view: MyComicsContract.View
    @Mock
    private lateinit var manga: Manga
    @Mock
    private lateinit var response: MangasResponse
    @Mock
    private lateinit var exception: Exception

    private lateinit var presenter: MyComicsContract.Presenter

    @Captor
    private lateinit var getMyMangasCallbackCaptor:
        ArgumentCaptor<OnLoadedDataCallback<MangasResponse>>

    @Captor
    private lateinit var deleteMangaCallbackCaptor:
        ArgumentCaptor<OnLoadedDataCallback<Boolean>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = MyComicsPresenter(view, repository)
    }

    @Test
    fun getMyMangasSuccessfulTest() {
        presenter.getMyMangas()
        Mockito.verify(repository).getMyMangas(
            capture(getMyMangasCallbackCaptor)
        )
        getMyMangasCallbackCaptor.value.onSuccessful(response)
        Mockito.verify(view).showMangas(response.mangas)
    }

    @Test
    fun getMyMangasFailedTest() {
        presenter.getMyMangas()
        Mockito.verify(repository).getMyMangas(
            capture(getMyMangasCallbackCaptor)
        )
        getMyMangasCallbackCaptor.value.onFailed(exception)
        Mockito.verify(view).showError(exception)
    }

    @Test
    fun deleteMangaSuccessfulTest() {
        presenter.deleteManga(manga)
        Mockito.verify(repository).deleteManga(
            eq(manga),
            capture(deleteMangaCallbackCaptor)
        )
        deleteMangaCallbackCaptor.value.onSuccessful(true)
        Mockito.verify(view).confirmDeleted(true)
    }


    @Test
    fun deleteMangaFailedTest() {
        presenter.deleteManga(manga)
        Mockito.verify(repository).deleteManga(
            eq(manga),
            capture(deleteMangaCallbackCaptor)
        )
        deleteMangaCallbackCaptor.value.onFailed(exception)
        Mockito.verify(view).showError(exception)
    }
}
