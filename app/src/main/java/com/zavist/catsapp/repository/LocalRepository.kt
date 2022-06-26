package com.zavist.catsapp.repository

import androidx.room.Transaction
import com.zavist.catsapp.db.CatsDatabase
import com.zavist.catsapp.db.entity.BreedEntity
import com.zavist.catsapp.db.entity.FavoriteEntity
import com.zavist.catsapp.db.entity.RemoteKeyEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(
    private val database: CatsDatabase
) {

    //Breeds
    fun getAllBreeds() = database.breedsDao().getAllBreeds()
    fun getAllBreedsPaging() = database.breedsDao().getAllBreedsPaging()
    fun insert(breed: BreedEntity) = database.breedsDao().insert(breed)
    fun insertAll(breeds: List<BreedEntity>) = database.breedsDao().insertAll(breeds)
    fun getBreed(id: String) = database.breedsDao().getBreed(id)

    //RemoteKey
    fun getKey(type: String) = database.remoteKeyDao().getKey(type)
    fun insert(remoteKey: RemoteKeyEntity) = database.remoteKeyDao().insert(remoteKey)
    fun update(remoteKey: RemoteKeyEntity) = database.remoteKeyDao().update(remoteKey)

    //Favorites
    fun insertAllFavorites(favoriteEntities: List<FavoriteEntity>) = database.favoriteDao().insertAll(favoriteEntities)
    fun insert(favoriteEntity: FavoriteEntity) = database.favoriteDao().insert(favoriteEntity)
    fun getFavorites() = database.favoriteDao().getFavorites()
    fun getFavorite(imageId: String) = database.favoriteDao().getFavorite(imageId)
    fun delete(favoriteEntity: FavoriteEntity) = database.favoriteDao().delete(favoriteEntity)
    fun getFavoritesCount() = database.favoriteDao().getFavoritesCount()

    @Transaction
    fun insertKeyAndItems(keyEntity: RemoteKeyEntity, breeds: List<BreedEntity>) {
        insert(keyEntity)
        insertAll(breeds)
    }

    @Transaction
    fun deleteKeyAndBreeds() {
        database.remoteKeyDao().delete(RemoteKeyEntity("BREED"))
        database.breedsDao().deleteAll()
    }

}