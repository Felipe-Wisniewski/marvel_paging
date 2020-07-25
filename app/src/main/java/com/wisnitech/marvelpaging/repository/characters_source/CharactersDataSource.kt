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

class CharactersDataSource(private val api: ApiService, private val scope: CoroutineScope) :
    PageKeyedDataSource<Int, CharacterWeb>() {

    private var supervisorJob = SupervisorJob()

    private val _charactersStatus = MutableLiveData(Status.LOADING)
    val charactersStatus: LiveData<Status> get() = _charactersStatus

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
            val response = api.getCharacters(requestedPage * loadedPage)
            Log.d("flmwg","results.size: ${response.data.results.size}")
            _charactersStatus.postValue(Status.SUCCESS)
            callback(response.data.results)
        }
    }

    private fun jobError() = CoroutineExceptionHandler { _, throwable ->
        Log.e("FLMWG", "CharactersDataSource.jobError", throwable)
        _charactersStatus.postValue(Status.ERROR)
    }
}