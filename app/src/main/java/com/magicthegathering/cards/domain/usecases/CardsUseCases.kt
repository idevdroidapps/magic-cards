package com.magicthegathering.cards.domain.usecases

import androidx.paging.PagingData
import com.magicthegathering.cards.domain.entities.MagicCard
import com.magicthegathering.cards.domain.interfaces.CardsRepository
import kotlinx.coroutines.flow.Flow

class CardsUseCases(private val repository: CardsRepository) {

    fun searchCards(query: String?): Flow<PagingData<MagicCard>> {
        return repository.fetchCards(query)
    }

}