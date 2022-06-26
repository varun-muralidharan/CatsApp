package com.zavist.catsapp.ui.detail.model

sealed class DetailUi {

    data class BreedDetail(
        val id: String,
        val name: String,
        val description: String,
        val wikiUrl: String?,
        val imageId: String?,
        val imageUrl: String?,
        var favoriteId: Int = -1
    ): DetailUi()

    data class Speciality(val name: String, val score: Int): DetailUi()


}
