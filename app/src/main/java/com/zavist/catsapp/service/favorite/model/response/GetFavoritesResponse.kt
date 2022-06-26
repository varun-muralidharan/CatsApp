package com.zavist.catsapp.service.favorite.model.response
import androidx.annotation.Keep


@Keep
data class GetFavoritesResponse(
    val id: Int,
    val user_id: String,
    val image_id: String,
    val sub_id: String,
    val created_at: String,
    val image: Image
)

@Keep
data class Image(
    val id: String,
    val url: String
)