package com.zavist.catsapp.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.zavist.catsapp.db.entity.FavoriteEntity
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
abstract class FavoriteDao: BaseDao<FavoriteEntity> {

    @Query("SELECT * FROM FavoriteEntity")
    abstract fun getFavorites(): Flowable<List<FavoriteEntity>>

    @Query("SELECT COUNT(*) FROM FavoriteEntity")
    abstract fun getFavoritesCount(): Single<Int>

    @Query("SELECT * FROM FavoriteEntity WHERE imageId=:imageId")
    abstract fun getFavorite(imageId: String): Single<FavoriteEntity>

}
