package com.wisnitech.marvelpaging.model

data class ResponseCharacter(val data: DataCharacter)

data class DataCharacter(val results: List<CharacterWeb>)

data class CharacterWeb(
    val id: Long,
    val name: String,
    val description: String,
    val thumbnail: ThumbnailCharacter
)

data class ThumbnailCharacter(
    val path: String,
    val extension: String
)