package com.zavist.catsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import com.zavist.catsapp.db.dao.BreedsDao
import com.zavist.catsapp.db.dao.FavoriteDao
import com.zavist.catsapp.db.dao.RemoteKeyDao
import com.zavist.catsapp.db.entity.BreedEntity
import com.zavist.catsapp.db.entity.FavoriteEntity
import com.zavist.catsapp.db.entity.RemoteKeyEntity

@TypeConverters(CatsDatabaseConverters::class)
@Database(
    entities = [
        BreedEntity::class,
        RemoteKeyEntity::class,
        FavoriteEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class CatsDatabase : RoomDatabase() {

    abstract fun breedsDao(): BreedsDao
    abstract fun remoteKeyDao(): RemoteKeyDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {

        private val migrations: Array<Migration> by lazy { emptyArray() }
        private lateinit var INSTANCE: CatsDatabase

        @JvmStatic
        fun getInstance(context: Context): CatsDatabase {
            if (!Companion::INSTANCE.isInitialized) {
                synchronized(CatsDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        CatsDatabase::class.java, "CatsDatabase.db"
                    )
                        .fallbackToDestructiveMigration()
//                        .addMigrations(*migrations)
                        .build()
                }
            }
            return INSTANCE
        }
    }

}