package com.magicthegathering.cards.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.magicthegathering.cards.data.network.MtgService
import com.magicthegathering.cards.data.network.CardsPagingSource
import com.magicthegathering.cards.domain.entities.MagicCard
import com.magicthegathering.cards.domain.interfaces.CardsRepository
import kotlinx.coroutines.flow.Flow

class CardsRepositoryImpl(private val MtgService: MtgService) : CardsRepository {

    override fun fetchCards(query: String?): Flow<PagingData<MagicCard>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { CardsPagingSource(MtgService, query) }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20

        // For Singleton instantiation
        @Volatile
        private var instance: CardsRepositoryImpl? = null

        fun getInstance(service: MtgService) =
            instance ?: synchronized(this) {
                instance ?: CardsRepositoryImpl(service).also { instance = it }
            }
    }
}