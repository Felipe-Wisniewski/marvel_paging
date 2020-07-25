package com.wisnitech.marvelpaging.repository.characters_source

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.wisnitech.marvelpaging.model.CharacterWeb
import com.wisnitech.marvelpaging.repository.service.ApiService
import kotlinx.coroutines.CoroutineScope

class CharactersDataSourceFactory(private val api: ApiService, private val scope: CoroutineScope) : DataSource.Factory<Int, CharacterWeb>() {

    val source = MutableLiveData<CharactersDataSource>()

    override fun create(): DataSource<Int, CharacterWeb> {
        val src = CharactersDataSource(api, scope)
        source.postValue(src)
        return src
    }
}