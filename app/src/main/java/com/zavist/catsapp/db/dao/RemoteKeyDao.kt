package com.zavist.catsapp.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.zavist.catsapp.db.entity.RemoteKeyEntity
import io.reactivex.rxjava3.core.Single

@Dao
abstract class RemoteKeyDao : BaseDao<RemoteKeyEntity> {

    @Query("SELECT * FROM RemoteKeyEntity WHERE type=:type")
    abstract fun getKey(type: String): Single<RemoteKeyEntity>

}