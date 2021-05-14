package com.magicthegathering.cards.presentation.viewmodels

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

    fun searchCards(query: String): Flow<PagingData<MagicCard>> {
        val lastResult = mCurrentSearchResult
        if (query == mPreviousQuery && lastResult != null) {
            return lastResult
        }
        val newResult: Flow<PagingData<MagicCard>> =
            cardsUseCases.searchCards(query)
                .cachedIn(viewModelScope)
        mPreviousQuery = query
        mCurrentSearchResult = newResult
        return newResult
    }
}