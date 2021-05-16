package com.magicthegathering.cards.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.magicthegathering.cards.data.db.CardRemoteKeys
import com.magicthegathering.cards.data.db.CardsDatabase
import com.magicthegathering.cards.data.network.MtgService
import com.magicthegathering.cards.data.repositories.CardsRepositoryImpl.Companion.DEFAULT_PAGE_INDEX
import com.magicthegathering.cards.domain.entities.MagicCard
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CardRemoteMediator(
    private val query: String?,
    private val cardService: MtgService,
    private val cardsDatabase: CardsDatabase
) : RemoteMediator<Int, MagicCard>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MagicCard>
    ): MediatorResult {

        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> return pageKeyData
            else -> pageKeyData as Int
        }

        try {
            val response = cardService.searchCards(query, page, state.config.pageSize)
            val endOfPaginationReached = response.cards.isEmpty()
            cardsDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    cardsDatabase.remoteKeysDao.clearRemoteKeys()
                    cardsDatabase.cardsDao.clearAllCards()
                }
                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = response.cards.map {
                    CardRemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                cardsDatabase.remoteKeysDao.insertAll(keys)
                cardsDatabase.cardsDao.insertCards(response.cards)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    /**
     * this returns the page key or the final end of list success result
     */
    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, MagicCard>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }
    }

    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, MagicCard>): CardRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { magicCard ->
                cardsDatabase.remoteKeysDao.remoteKeysCardId(magicCard.id)
            }
    }

    /**
     * get the last remote key inserted which had the data
     */
    private suspend fun getLastRemoteKey(state: PagingState<Int, MagicCard>): CardRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { magicCard ->
                cardsDatabase.remoteKeysDao.remoteKeysCardId(magicCard.id)
            }
    }

    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getClosestRemoteKey(state: PagingState<Int, MagicCard>): CardRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { cardId ->
                cardsDatabase.remoteKeysDao.remoteKeysCardId(cardId)
            }
        }
    }
}