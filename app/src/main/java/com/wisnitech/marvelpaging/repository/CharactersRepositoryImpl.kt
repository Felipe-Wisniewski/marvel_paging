package com.wisnitech.marvelpaging.repository

import androidx.lifecycle.Transformations.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.wisnitech.marvelpaging.model.CharacterWeb
import com.wisnitech.marvelpaging.repository.characters_source.CharactersDataSourceFactory
import com.wisnitech.marvelpaging.repository.service.ApiService
import kotlinx.coroutines.CoroutineScope

class CharactersRepositoryImpl(private val api: ApiService, private val scope: CoroutineScope) :
    CharactersRepository {

    override fun getCharacters(query: String): Listing<CharacterWeb> {
        val charactersSource = CharactersDataSourceFactory(api, query, scope)

        val pagedList = LivePagedListBuilder(charactersSource, PagedList.Config.Builder()
            .setPageSize(20)
            .setInitialLoadSizeHint(40)
            .setPrefetchDistance(5)
            .setEnablePlaceholders(false)
            .build()).build()

        val loadingStatus = switchMap(charactersSource.source) {
            it.charactersStatus
        }

        return Listing(pagedList = pagedList, listingStatus = loadingStatus)
    }
}