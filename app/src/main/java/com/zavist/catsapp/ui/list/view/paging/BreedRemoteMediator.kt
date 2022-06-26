package com.zavist.catsapp.ui.list.view.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxRemoteMediator
import com.zavist.catsapp.db.entity.BreedEntity
import com.zavist.catsapp.db.entity.RemoteKeyEntity
import com.zavist.catsapp.repository.LocalRepository
import com.zavist.catsapp.repository.NetworkRepository
import com.zavist.catsapp.util.CLog
import com.zavist.catsapp.util.DEFAULT_PAGE_SIZE
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@OptIn(ExperimentalPagingApi::class)
//@Singleton
class BreedRemoteMediator @Inject constructor(
    private val localRepository: LocalRepository,
    private val networkRepository: NetworkRepository
) : RxRemoteMediator<Int, BreedEntity>() {

    private val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
    override fun initializeSingle(): Single<InitializeAction> {
        return localRepository.getKey(BREED)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .onErrorResumeNext {
                CLog.d(TAG, "onErrorResumeNext", it.message)
                Single.just(RemoteKeyEntity(BREED,0,false,0))
            }
            .flatMap {
                if (System.currentTimeMillis() - it.updatedTime >= cacheTimeout) {
                    Single.just(InitializeAction.LAUNCH_INITIAL_REFRESH)
                } else {
                    Single.just(InitializeAction.SKIP_INITIAL_REFRESH)
                }
            }.onErrorResumeNext {
                CLog.d(TAG, "onErrorResumeNext", it.message)
                Single.just(InitializeAction.LAUNCH_INITIAL_REFRESH)
            }
    }

    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, BreedEntity>
    ): Single<MediatorResult> {
        CLog.d(
            TAG,
            "loadSingle",
            "loadType: $loadType, state pageSize: ${state.pages.size}, AnchorPosition: ${state.anchorPosition}"
        )
        var remoteKey = Single.just(RemoteKeyEntity(BREED))
        when (loadType) {
            LoadType.REFRESH -> {}
            LoadType.PREPEND -> return Single.just(MediatorResult.Success(endOfPaginationReached = true))
            LoadType.APPEND -> remoteKey = localRepository.getKey(BREED)
        }

        return remoteKey.subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .flatMap { key ->
                CLog.d(TAG, "loadSingle", "remoteKey: $key")
                if (key.lastPage) {
                    Single.just(MediatorResult.Success(endOfPaginationReached = true))
                } else {
                    networkRepository
                        .getAllBreeds(limit = DEFAULT_PAGE_SIZE, page = key.nextPage)
                        .map<MediatorResult> {
                            CLog.d(TAG, "loadSingle", "breeds list size:${it.size}")
                            if (it.isEmpty()) {
                                localRepository.update(key.also {
                                    it.lastPage = true
                                    it.updatedTime = System.currentTimeMillis()
                                })
                                MediatorResult.Success(endOfPaginationReached = true)
                            } else {
                                if (loadType == LoadType.REFRESH) {
                                    localRepository.deleteKeyAndBreeds()
                                }
                                localRepository.insertKeyAndItems(
                                    key.also {
                                        it.nextPage += 1
                                        it.updatedTime = System.currentTimeMillis()
                                    }, it
                                )
                                MediatorResult.Success(endOfPaginationReached = false)
                            }
                        }
                }
            }.onErrorResumeNext {
                CLog.d(TAG, "onErrorResumeNext", it.message)
                if (it is IOException || it is HttpException) {
                    Single.just(MediatorResult.Error(it))
                } else Single.error(it)
            }
    }

    companion object {
        private const val TAG = "[CAT]BreedRemoteMediator"
        private const val BREED = "BREED"
    }
}
