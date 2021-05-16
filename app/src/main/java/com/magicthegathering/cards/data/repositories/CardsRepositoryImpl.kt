package com.magicthegathering.cards.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.magicthegathering.cards.data.db.CardsDatabase
import com.magicthegathering.cards.data.network.CardsPagingSource
import com.magicthegathering.cards.data.network.MtgService
import com.magicthegathering.cards.domain.entities.MagicCard
import com.magicthegathering.cards.domain.interfaces.CardsRepository
import kotlinx.coroutines.flow.Flow

class CardsRepositoryImpl(
    private val mtgService: MtgService,
    private val cardsDatabase: CardsDatabase
) : CardsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun fetchCards(query: String?): Flow<PagingData<MagicCard>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { CardsPagingSource(mtgService, query) },
            remoteMediator = CardRemoteMediator(query, mtgService, cardsDatabase)
        ).flow
    }

    companion object {
        const val DEFAULT_PAGE_INDEX = 0
        const val NETWORK_PAGE_SIZE = 9

        // For Singleton instantiation
        @Volatile
        private var instance: CardsRepositoryImpl? = null

        fun getInstance(service: MtgService, cardsDatabase: CardsDatabase) =
            instance ?: synchronized(this) {
                instance ?: CardsRepositoryImpl(service, cardsDatabase).also { instance = it }
            }
    }
}