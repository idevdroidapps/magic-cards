package com.magicthegathering.cards.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.magicthegathering.cards.domain.entities.MagicCard
import retrofit2.HttpException
import java.io.IOException

private const val CARDS_STARTING_PAGE_INDEX = 0

class CardsPagingSource(private val MtgService: MtgService, private val query: String?): PagingSource<Int, MagicCard>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MagicCard> {
        val pageNum = params.key ?: CARDS_STARTING_PAGE_INDEX
        return try {
            val response = MtgService.searchCards(query, pageNum, params.loadSize)
            val cards = response.cards
            LoadResult.Page(
                data = cards,
                prevKey = if (pageNum == CARDS_STARTING_PAGE_INDEX) null else pageNum,
                nextKey = if (cards.isEmpty()) null else (pageNum + params.loadSize)
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MagicCard>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}