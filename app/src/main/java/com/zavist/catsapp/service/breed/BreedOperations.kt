package com.zavist.catsapp.service.breed

import com.zavist.catsapp.db.entity.BreedEntity
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface BreedOperations {

    @GET("v1/breeds")
    fun getBreeds(
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): Single<List<BreedEntity>>

    @GET("v1/breeds")
    fun getBreeds(): Flowable<List<BreedEntity>>

}