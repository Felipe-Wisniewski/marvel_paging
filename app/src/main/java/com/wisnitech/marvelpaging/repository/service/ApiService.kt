package com.wisnitech.marvelpaging.repository.service

import com.wisnitech.marvelpaging.model.ResponseCharacter
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("characters")
    suspend fun getCharacters(
        @Query("offset") offset: Int? = 0,
        @Query("orderBy") orderBy: String = "-modified",
        @Query("nameStartsWith") nameStartsWith: String? = null
    ): ResponseCharacter

}