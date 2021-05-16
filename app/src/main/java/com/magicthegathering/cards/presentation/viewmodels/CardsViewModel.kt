package com.magicthegathering.cards.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.magicthegathering.cards.domain.entities.MagicCard
import com.magicthegathering.cards.domain.usecases.CardsUseCases
import kotlinx.coroutines.flow.Flow

class CardsViewModel(private val cardsUseCases: CardsUseCases) : ViewModel() {

    private var mCurrentSearchResult: Flow<PagingData<MagicCard>>? = null
    private var mPreviousQuery: String? = null

    val currentQuery: LiveData<String?> get() = mCurrentQuery
    private var mCurrentQuery = MutableLiveData<String?>()

    val currentCard: LiveData<MagicCard> get() = mCurrentCard
    private var mCurrentCard = MutableLiveData<MagicCard>()

    val startClearCacheWorker: LiveData<Boolean> get() = mStartClearCacheWorker
    private var mStartClearCacheWorker = MutableLiveData<Boolean>()

    fun searchCards(query: String?): Flow<PagingData<MagicCard>> {
        val newResult: Flow<PagingData<MagicCard>> =
            cardsUseCases.searchCards(query)
                .cachedIn(viewModelScope)
        mPreviousQuery = query
        mCurrentSearchResult = newResult
        mStartClearCacheWorker.postValue(true)
        return newResult
    }

    /**
     * Sets the value for current repo LiveData
     *
     * @param   card  The currently selected [MagicCard]
     */
    fun setCurrentCard(card: MagicCard) {
        mCurrentCard.value = card
    }

    /**
     * Sets the value for current query LiveData
     *
     * @param   queryString  The query term to search, as a [String]
     */
    fun setCurrentQuery(queryString: String?) {
        mCurrentQuery.value = queryString
    }

    /**
     * Sets the LiveData value for starting ClearCacheWorker
     */
    fun setClearCacheWorker(start: Boolean) {
        mStartClearCacheWorker.value = start
    }
}