package com.zavist.catsapp.service

import com.zavist.catsapp.service.breed.BreedOperations
import com.zavist.catsapp.service.category.CategoryOperations
import com.zavist.catsapp.service.favorite.FavoriteOperations
import com.zavist.catsapp.service.image.ImageOperations
import com.zavist.catsapp.service.vote.VoteOperations

interface CatsService :
    BreedOperations,
    CategoryOperations,
    FavoriteOperations,
    ImageOperations,
    VoteOperations {}