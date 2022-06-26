package com.zavist.catsapp.ui.detail.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zavist.catsapp.db.entity.BreedEntity
import com.zavist.catsapp.repository.LocalRepository
import com.zavist.catsapp.util.CLog
import com.zavist.catsapp.util.FALLBACK_IMAGE_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val localRepository: LocalRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _breed = MutableLiveData<List<DetailUi>>()
    val breed: LiveData<List<DetailUi>> = _breed

    fun load(id: String?, favoriteId: Int?) {
        if (!id.isNullOrBlank()) {
            compositeDisposable.add(
                localRepository.getBreed(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map { it.toBreedDetail(favoriteId) }
                    .subscribe(
                        {
                            CLog.d(TAG, "load", "onSuccess: breed: $it")
                            _breed.postValue(it)
                        },
                        { CLog.d(TAG, "load", "onError: ${it.message}") },
                    )
            )

            compositeDisposable.add(
                localRepository.getFavorites()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .distinctUntilChanged()
                    .subscribe(
                        { entities ->
                            CLog.d(TAG, "load", "onNext: $entities")
                            val entity = entities.firstOrNull { it.imageId == id }
                            val detail = _breed.value?.filterIsInstance<DetailUi.BreedDetail>()
                                ?.firstOrNull()
                                ?.copy(favoriteId = entity?.favoriteID ?: -1)
                            detail?.let {
                                _breed.postValue(_breed.value?.toMutableList()?.apply {
                                    removeAt(0)
                                    add(0, detail)
                                })
                            }
                        },
                        { CLog.d(TAG, "load", "onError: ${it.message}") },
                        { CLog.d(TAG, "load", "onComplete") }
                    )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        private const val TAG = "[CAT]DetailViewModel"
    }
}

private fun BreedEntity.toBreedDetail(favoriteId: Int?): List<DetailUi> {
    return mutableListOf<DetailUi>(
        DetailUi.BreedDetail(
            id,
            name,
            description,
            wikipedia_url,
            image?.id,
            image?.url ?: FALLBACK_IMAGE_URL,
            favoriteId ?: -1
        )
    ).apply {
        addAll(
            listOfNotNull(
                adaptability?.let { DetailUi.Speciality("Adaptability", adaptability) },
                affection_level?.let { DetailUi.Speciality("Affection level", affection_level) },
                child_friendly?.let { DetailUi.Speciality("Child friendly", child_friendly) },
                dog_friendly?.let { DetailUi.Speciality("Dog friendly", dog_friendly) },
                energy_level?.let { DetailUi.Speciality("Energy level", energy_level) },
                grooming?.let { DetailUi.Speciality("Grooming", grooming) },
                health_issues?.let { DetailUi.Speciality("Health Issues", health_issues) },
                intelligence?.let { DetailUi.Speciality("Intelligence", intelligence) },
                shedding_level?.let { DetailUi.Speciality("Shedding Level", shedding_level) },
                social_needs?.let { DetailUi.Speciality("Social Needs", social_needs) },
                stranger_friendly?.let {
                    DetailUi.Speciality(
                        "Stranger Friendly",
                        stranger_friendly
                    )
                },
                vocalisation?.let { DetailUi.Speciality("Vocalisation", vocalisation) }
            ).filter { it.score != 0 }
                .sortedByDescending { it.score }
        )
    }
}
