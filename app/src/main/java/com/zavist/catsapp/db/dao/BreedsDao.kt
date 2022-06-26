package com.zavist.catsapp.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.zavist.catsapp.db.entity.BreedEntity
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
abstract class BreedsDao : BaseDao<BreedEntity> {

    @Query("SELECT * FROM BreedEntity WHERE id=:id")
    abstract fun getBreed(id: String): Single<BreedEntity>

    @Query("SELECT * FROM BreedEntity")
    abstract fun getAllBreeds(): Flowable<List<BreedEntity>>

    @Query("SELECT * FROM BreedEntity")
    abstract fun getAllBreedsPaging(): PagingSource<Int, BreedEntity>

    @Query("DELETE FROM BreedEntity")
    abstract fun deleteAll()
}