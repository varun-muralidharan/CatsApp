package com.zavist.catsapp.db.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity
data class RemoteKeyEntity(
    @PrimaryKey
    val type: String,
    var nextPage: Int = 0,
    var lastPage: Boolean = false,
    var updatedTime: Long = System.currentTimeMillis()
)
