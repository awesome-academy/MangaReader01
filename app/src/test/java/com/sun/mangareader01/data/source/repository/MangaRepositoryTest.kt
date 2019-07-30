package com.sun.mangareader01.data.source.repository

import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.sun.mangareader01.data.model.Manga
import com.sun.mangareader01.data.model.MangaDetail
import com.sun.mangareader01.data.model.MangasResponse
import com.sun.mangareader01.data.source.MangaDataSource
import com.sun.mangareader01.data.source.local.OnLoadedDataCallback
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MangaRepositoryTest {

    @Mock
    private lateinit var remote: MangaDataSource.Remote
    @Mock
    private lateinit var local: MangaDataSource.Local
    @Mock
    private lateinit var manga: Manga
    @Mock
    private lateinit var repository: MangaRepository
    @Mock
    private lateinit var mangasResponseCallback: OnLoadedDataCallback<MangasResponse>
    @Mock
    private lateinit var mangaDetailCallback: OnLoadedDataCallback<MangaDetail>
    @Mock
    private lateinit var updateDbCallback: OnLoadedDataCallback<Boolean>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = MangaRepository.apply {
            initDataSource(remote, local)
        }
    }

    @Test
    fun getMangasTest() {
        repository.getMangas(QUERY, mangasResponseCallback)
        verify(remote).getMangas(eq(QUERY), eq(mangasResponseCallback))
    }

    @Test
    fun getMangaDetailTest() {
        repository.getMangaDetail(manga, mangaDetailCallback)
        verify(remote).getMangaDetail(eq(manga), eq(mangaDetailCallback))
    }

    @Test
    fun getFilteredMangasWithOnlyCategoryTest() {
        repository.getFilteredMangas(
            category = SAMPLE_CATEGORY,
            callback = mangasResponseCallback
        )
        verify(remote).getFilteredMangas(
            category = SAMPLE_CATEGORY,
            callback = mangasResponseCallback
        )
    }

    @Test
    fun getMyMangasTest() {
        repository.getMyMangas(mangasResponseCallback)
        verify(local).getMyMangas(eq(mangasResponseCallback))
    }

    @Test
    fun updateMangaTest() {
        repository.updateManga(manga, updateDbCallback)
        verify(local).updateManga(eq(manga), eq(updateDbCallback))
    }

    companion object {
        private const val QUERY = "QUERY"
        private const val SAMPLE_CATEGORY = "1"
    }
}
