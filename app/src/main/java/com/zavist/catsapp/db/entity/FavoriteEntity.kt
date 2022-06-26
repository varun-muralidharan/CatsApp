package com.zavist.catsapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteEntity(@PrimaryKey val imageId: String, val favoriteID: Int)