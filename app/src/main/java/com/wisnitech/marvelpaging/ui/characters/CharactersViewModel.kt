package com.wisnitech.marvelpaging.ui.characters

import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.wisnitech.marvelpaging.model.CharacterWeb
import com.wisnitech.marvelpaging.repository.characters_source.CharactersDataSourceFactory
import com.wisnitech.marvelpaging.repository.service.Status
import org.koin.java.KoinJavaComponent.inject

class CharactersViewModel : ViewModel() {

    private var showBalanceControl = false      // SHAREDPREFERENCES

    private val charactersSource: CharactersDataSourceFactory by inject(CharactersDataSourceFactory::class.java)

    private val _showBalance = MutableLiveData(false)
    val showBalance: LiveData<Boolean> get() = _showBalance

    private val _balance = MutableLiveData("")
    val balance: LiveData<String> = _balance

    private val _loadingBalance = MutableLiveData<Status>()
    val loadingBalance: LiveData<Status> = _loadingBalance

    private val _characters = LivePagedListBuilder(charactersSource, pagedListConfig()).build()
    val characters: LiveData<PagedList<CharacterWeb>> get() = _characters

    private val _charactersStatus = switchMap(charactersSource.source) { it.charactersStatus }
    val charactersStatus: LiveData<Status> get() = _charactersStatus

    private val _isSearching = MutableLiveData(false)
    val isSearching: LiveData<Boolean> get() = _isSearching

    fun showBalance() {
        showBalanceControl = !showBalanceControl
        _showBalance.value = showBalanceControl
    }

    fun isSearching(s: Boolean) {
        _isSearching.value = s
    }

    fun getBalance() {
        _showBalance.value = showBalanceControl

        _loadingBalance.value = Status.LOADING

        Handler().postDelayed({
            _balance.value = "$ 41.123,99"
            _loadingBalance.value = Status.SUCCESS
        },300)
    }

    fun refreshCharacters() {
        charactersSource.source.value?.invalidate()
    }

    fun searchOperations(query: String = "") {
        Log.d("flmwg","search query: $query")
    }

    private fun pagedListConfig() = PagedList.Config.Builder()
        .setPageSize(20)
        .setInitialLoadSizeHint(40)
        .setPrefetchDistance(5)
        .setEnablePlaceholders(false)
        .build()
}