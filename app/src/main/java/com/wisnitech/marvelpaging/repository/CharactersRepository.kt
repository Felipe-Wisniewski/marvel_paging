package com.wisnitech.marvelpaging.repository

import com.wisnitech.marvelpaging.model.CharacterWeb

interface CharactersRepository {
    fun getCharacters(query: String): Listing<CharacterWeb>
}