package com.zavist.catsapp.service.favorite

import com.zavist.catsapp.service.favorite.model.request.SetFavoriteRequest
import com.zavist.catsapp.service.favorite.model.response.GetFavoritesResponse
import com.zavist.catsapp.service.favorite.model.response.RemoveFavoriteResponse
import com.zavist.catsapp.service.favorite.model.response.SetFavoriteResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface FavoriteOperations {

    @GET("v1/favourites")
    fun getAllFavorites(@Query("sub_id") userId: String): Single<List<GetFavoritesResponse>>

    @POST("v1/favourites")
    fun setFavorite(@Body request: SetFavoriteRequest): Single<SetFavoriteResponse>

    @DELETE("v1/favourites/{favouriteId}")
    fun removeFavorite(@Path("favouriteId") favoriteId: Int): Single<RemoveFavoriteResponse>
}