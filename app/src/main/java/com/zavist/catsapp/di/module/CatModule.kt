package com.zavist.catsapp.di.module

import android.content.Context
import android.content.SharedPreferences
import com.zavist.catsapp.BuildConfig
import com.zavist.catsapp.db.CatsDatabase
import com.zavist.catsapp.service.CatsService
import com.zavist.catsapp.util.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CatModule {

    private const val TAG = "[CAT][HILT]CatModule"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        CLog.d(TAG, "provideOkHttpClient", "")
        return (if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder().addInterceptor(loggingInterceptor)
        } else {
            OkHttpClient.Builder()
        })
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .addHeader(HEADER_API_KEY, SERVER_API_KEY)
                        .addHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE)
                        .addHeader(HEADER_USER_ID, HEADER_USER_ID_VALUE)
                        .build()
                )
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonFactory(): GsonConverterFactory {
        CLog.d(TAG, "provideGsonFactory", "")
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        CLog.d(TAG, "provideRetrofit", "")
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(SERVER_ENDPOINT_PROD)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCatService(retrofit: Retrofit): CatsService {
        CLog.d(TAG, "provideCatService", "")
        return retrofit.create(CatsService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CatsDatabase {
        CLog.d(TAG, "provideDatabase", "")
        return CatsDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        CLog.d(TAG, "provideSharedPreferences", "")
        return context.getSharedPreferences("CatPreferences", Context.MODE_PRIVATE)
    }

}