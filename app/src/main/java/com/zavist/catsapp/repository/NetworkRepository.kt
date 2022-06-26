package com.zavist.catsapp.repository

import com.zavist.catsapp.service.CatsService
import com.zavist.catsapp.service.favorite.model.request.SetFavoriteRequest
import com.zavist.catsapp.util.HEADER_USER_ID_VALUE
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
    private val restClient: CatsService
) {

    //Breed
    fun getAllBreeds(limit: Int, page: Int) = restClient.getBreeds(limit, page)
    fun getAllBreeds() = restClient.getBreeds()

    //Favorite
    fun setFavorite(imageId: String) = restClient.setFavorite(SetFavoriteRequest(imageId, HEADER_USER_ID_VALUE))
    fun removeFavorite(favoriteId: Int) = restClient.removeFavorite(favoriteId)
    fun getAllFavorites() = restClient.getAllFavorites(HEADER_USER_ID_VALUE)

}