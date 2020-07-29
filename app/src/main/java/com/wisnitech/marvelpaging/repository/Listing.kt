package com.wisnitech.marvelpaging.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.wisnitech.marvelpaging.repository.service.Status

data class Listing<T>(
    val pagedList: LiveData<PagedList<T>>,
    val listingStatus: LiveData<Status>
)