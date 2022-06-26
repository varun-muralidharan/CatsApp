package com.zavist.catsapp.ui.list.model

data class Breed(
    val id: String,
    val name: String,
    val description: String,
    val wikiLink: String,
    val imageId: String?,
    val imageUrl: String,
    var favoriteId: Int = -1
)