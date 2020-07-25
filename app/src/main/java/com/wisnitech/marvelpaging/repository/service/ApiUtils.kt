package com.wisnitech.marvelpaging.repository.service

import java.math.BigInteger
import java.security.MessageDigest

fun getHash(ts: String): String {
    val value = ts + API_KEY + PUBLIC_API_KEY
    return value.md5()
}

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}

enum class Status {
    DEFAULT,
    SUCCESS,
    ERROR,
    LOADING,
}