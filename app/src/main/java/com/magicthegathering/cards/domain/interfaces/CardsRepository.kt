package com.magicthegathering.cards.domain.interfaces

import androidx.paging.PagingData
import com.magicthegathering.cards.domain.entities.MagicCard
import kotlinx.coroutines.flow.Flow

interface CardsRepository {
    fun fetchCards(query: String?): Flow<PagingData<MagicCard>>
}