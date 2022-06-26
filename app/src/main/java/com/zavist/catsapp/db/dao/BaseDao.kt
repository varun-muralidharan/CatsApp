package com.zavist.catsapp.db.dao

import androidx.room.*

@Dao
interface BaseDao<E> {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insert(item: E): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: E)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<E>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnoreOnConflict(item: E)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnoreOnConflict(items: List<E>)

    @Update
    fun update(item: E)

    @Update
    fun update(items: List<E>)

    @Delete
    fun delete(item: E)

    @Delete
    fun delete(items: List<E>)

}