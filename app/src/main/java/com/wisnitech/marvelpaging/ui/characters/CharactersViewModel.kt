package com.wisnitech.marvelpaging.ui.characters

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.wisnitech.marvelpaging.model.CharacterWeb
import com.wisnitech.marvelpaging.repository.CharactersRepository
import com.wisnitech.marvelpaging.repository.service.Status

class CharactersViewModel(private val repo: CharactersRepository) : ViewModel() {

    private var showBalanceControl = false      // SHAREDPREFERENCES

    private val _showBalance = MutableLiveData(false)
    val showBalance: LiveData<Boolean> get() = _showBalance

    private val _balance = MutableLiveData("")
    val balance: LiveData<String> = _balance

    private val _loadingBalance = MutableLiveData<Status>()
    val loadingBalance: LiveData<Status> = _loadingBalance

    private val _charactersStatus = MutableLiveData<Status>()
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

    fun searchCharacters(query: String = ""): LiveData<PagedList<CharacterWeb>> {
        val repoResult = repo.getCharacters(query)

        repoResult.listingStatus.observeForever {
            _charactersStatus.value = it
        }

        return repoResult.pagedList
    }
}