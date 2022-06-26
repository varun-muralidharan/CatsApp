package com.zavist.catsapp.service.favorite.model.request

import com.google.gson.annotations.SerializedName

data class SetFavoriteRequest(
    @SerializedName("image_id")
    val imageId: String,
    @SerializedName("sub_id")
    val userId: String
)