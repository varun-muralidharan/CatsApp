package com.zavist.catsapp.ui.model

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.zavist.catsapp.db.entity.FavoriteEntity
import com.zavist.catsapp.repository.LocalRepository
import com.zavist.catsapp.repository.NetworkRepository
import com.zavist.catsapp.ui.detail.model.DetailUi
import com.zavist.catsapp.ui.list.model.Breed
import com.zavist.catsapp.util.CLog
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val networkRepository: NetworkRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var timestamp: Long = 0

    fun modifyFavorite(item: Breed) {
        modifyFavorite(item.imageId, item.favoriteId)
    }

    fun modifyFavorite(item: DetailUi.BreedDetail) {
        modifyFavorite(item.imageId, item.favoriteId)
    }

    private fun modifyFavorite(imageId: String?, favoriteId: Int) {
        if (favoriteId == -1) addToFavorites(imageId) else removeFromFavorites(imageId, favoriteId)
    }

    private fun removeFromFavorites(imageId: String?, favoriteId: Int) {
        imageId?.let {
            compositeDisposable.add(
                networkRepository.removeFavorite(favoriteId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(
                        {
                            CLog.d(TAG, "removeFromFavorites", "onSuccess: $it")
                            if (it.message == "SUCCESS") {
                                localRepository.delete(FavoriteEntity(imageId, favoriteId))
                            }
                        },
                        {
                            CLog.d(TAG, "removeFromFavorites", "onError: ${it.message}")
                        },
                    )
            )
        }
    }

    private fun addToFavorites(imageId: String?) {
        imageId?.let {
            compositeDisposable.add(
                networkRepository.setFavorite(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(
                        { response ->
                            CLog.d(TAG, "addToFavorites", "onSuccess: $response")
                            if (response.message == "SUCCESS") {
                                localRepository.insert(FavoriteEntity(imageId, response.id))
                            }
                        },
                        {
                            CLog.d(TAG, "addToFavorites", "onError: ${it.message}")
                        },
                    )
            )
        }
    }

    private fun checkForEmptyContent(performOperation: () -> Unit) {
        if (timestamp == 0L) {
            timestamp = sharedPreferences.getLong("FAVORITE_SYNC", 0)
        }
        compositeDisposable.add(
            localRepository.getFavoritesCount()
                .onErrorResumeNext {
                    CLog.d(TAG, "checkForEmptyContent", "onErrorResumeNext: ${it.message}")
                    Single.just(0)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    {
                        CLog.d(TAG, "checkForEmptyContent", "onSuccess: $it")
                        val timeElapsed = System.currentTimeMillis() - timestamp
                        if (it == 0 || timeElapsed > SYNC_TIME_GAP) {
                            performOperation.invoke()
                        }
                    },
                    {
                        CLog.d(TAG, "checkForEmptyContent", "onError: ${it.message}")
                    }
                )
        )
    }
    fun sync() {
        checkForEmptyContent {
            compositeDisposable.add(
                networkRepository.getAllFavorites()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(
                        { response ->
                            CLog.d(TAG, "sync", "onSuccess: ${response.size}")
                            localRepository.insertAllFavorites(response.map {
                                FavoriteEntity(
                                    it.image_id,
                                    it.id
                                )
                            })
                            timestamp = System.currentTimeMillis()
                            sharedPreferences.edit().putLong("FAVORITE_SYNC", timestamp).apply()
                        },
                        { CLog.d(TAG, "sync", "onError: ${it.message}") }
                    )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        private const val TAG = "[CAT]FavoriteViewModel"
        private val SYNC_TIME_GAP = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
    }
}