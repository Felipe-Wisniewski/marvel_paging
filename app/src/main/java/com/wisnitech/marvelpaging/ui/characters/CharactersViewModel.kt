package com.wisnitech.marvelpaging.ui.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.wisnitech.marvelpaging.model.CharacterWeb
import com.wisnitech.marvelpaging.repository.characters_source.CharactersDataSourceFactory
import com.wisnitech.marvelpaging.repository.service.Status
import org.koin.java.KoinJavaComponent.inject

class CharactersViewModel : ViewModel() {

    private val charactersSource: CharactersDataSourceFactory by inject(CharactersDataSourceFactory::class.java)

    private val _characters = LivePagedListBuilder(charactersSource, pagedListConfig()).build()
    val characters: LiveData<PagedList<CharacterWeb>> get() = _characters

    private val _charactersStatus = switchMap(charactersSource.source) { it.charactersStatus }
    val charactersStatus: LiveData<Status> get() = _charactersStatus

    fun refreshCharacters() {
        charactersSource.source.value?.invalidate()
    }

    private fun pagedListConfig() = PagedList.Config.Builder()
        .setPageSize(30)
        .setInitialLoadSizeHint(60)
        .setPrefetchDistance(15)
        .setEnablePlaceholders(false)
        .build()
}