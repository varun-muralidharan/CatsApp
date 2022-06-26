package com.zavist.catsapp.ui.list.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.paging.rxjava3.cachedIn
import androidx.paging.rxjava3.flowable
import com.zavist.catsapp.repository.LocalRepository
import com.zavist.catsapp.repository.NetworkRepository
import com.zavist.catsapp.ui.list.view.paging.BreedRemoteMediator
import com.zavist.catsapp.util.CLog
import com.zavist.catsapp.util.DEFAULT_PAGE_SIZE
import com.zavist.catsapp.util.FALLBACK_IMAGE_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.combineLatest
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

//TODO: Implement ViewState?

@HiltViewModel
class ListViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val networkRepository: NetworkRepository,
    private val remoteMediator: BreedRemoteMediator
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _breeds = MutableLiveData<PagingData<Breed>>()
    val breeds: LiveData<PagingData<Breed>> = _breeds

    @OptIn(ExperimentalPagingApi::class)
    fun load() {
        CLog.d(TAG, "doSomething", "")
        val pager = Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE),
            remoteMediator = remoteMediator
        ) { localRepository.getAllBreedsPaging() }

        pager.flowable
            .cachedIn(viewModelScope)
            .combineLatest(localRepository.getFavorites())
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .map { (breeds, favorites) ->
                breeds.map { cat ->
                    with(cat) {
                        Breed(
                            id = id,
                            name = name,
                            description = description,
                            wikiLink = wikipedia_url ?: "",
                            imageId = image?.id,
                            imageUrl = image?.url ?: FALLBACK_IMAGE_URL,
                            favoriteId = favorites.firstOrNull{ it.imageId == image?.id }?.favoriteID ?: -1
                        )
                    }
                }
            }.subscribe(
                {
                    CLog.d(TAG, "load", "onNext: ")
                    _breeds.postValue(it)
                },
                { CLog.d(TAG, "load", "onError: ${it.message}") },
                { CLog.d(TAG, "load", "onComplete") }
            )
//
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        private const val TAG = "[CAT]ListViewModel"
    }
}