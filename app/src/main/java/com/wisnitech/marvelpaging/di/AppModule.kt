package com.wisnitech.marvelpaging.di

import com.wisnitech.marvelpaging.repository.CharactersRepository
import com.wisnitech.marvelpaging.repository.CharactersRepositoryImpl
import com.wisnitech.marvelpaging.repository.characters_source.CharactersDataSourceFactory
import com.wisnitech.marvelpaging.repository.service.ApiServiceImpl
import com.wisnitech.marvelpaging.ui.characters.CharactersViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        ApiServiceImpl.getService()
    }

    single {
        CharactersRepositoryImpl(api = get(),
            scope = CoroutineScope(Dispatchers.IO)) as CharactersRepository
    }

    viewModel {
        CharactersViewModel(repo = get())
    }
}