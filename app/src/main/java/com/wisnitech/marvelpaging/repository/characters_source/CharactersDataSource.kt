package com.wisnitech.marvelpaging.repository.characters_source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.wisnitech.marvelpaging.model.CharacterWeb
import com.wisnitech.marvelpaging.repository.service.ApiService
import com.wisnitech.marvelpaging.repository.service.Status
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CharactersDataSource(private val api: ApiService,
                           private val query: String,
                           private val scope: CoroutineScope) :
    PageKeyedDataSource<Int, CharacterWeb>() {

    private var supervisorJob = SupervisorJob()

    val charactersStatus = MutableLiveData(Status.DEFAULT)

    override fun loadInitial(params: LoadInitialParams<Int>,
                             callback: LoadInitialCallback<Int, CharacterWeb>) {

        val items = params.requestedLoadSize

        loadCharacters(0, items) {
            callback.onResult(it, null, 1)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, CharacterWeb>) {
        val page = params.key
        val items = params.requestedLoadSize

        loadCharacters(page, items) {
            callback.onResult(it, page + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, CharacterWeb>) {
        val page = params.key
        val items = params.requestedLoadSize

        loadCharacters(page, items) {
            callback.onResult(it, page - 1)
        }
    }

    private fun loadCharacters(requestedPage: Int,
                               loadedPage: Int,
                               callback: (List<CharacterWeb>) -> Unit) {

        scope.launch(jobError() + supervisorJob) {

            Log.d("flmwg","query: $query")
            charactersStatus.postValue(Status.LOADING)

            val response = api.getCharacters(offset = requestedPage * loadedPage,
                nameStartsWith = if (query != "") query else null)

            Log.d("flmwg","results.size: ${response.data.results.size}")

            charactersStatus.postValue(Status.SUCCESS)
            callback(response.data.results)
        }
    }

    private fun jobError() = CoroutineExceptionHandler { _, throwable ->
        Log.e("FLMWG", "CharactersDataSource.jobError", throwable)
        charactersStatus.postValue(Status.ERROR)
    }
}