package com.wisnitech.marvelpaging.di

import com.wisnitech.marvelpaging.repository.characters_source.CharactersDataSourceFactory
import com.wisnitech.marvelpaging.repository.service.ApiServiceImpl
import com.wisnitech.marvelpaging.ui.characters.CharactersViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { ApiServiceImpl.getService() }
    single { CharactersDataSourceFactory(get(), CoroutineScope(Dispatchers.IO)) }
    viewModel { CharactersViewModel() }
}