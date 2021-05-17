package com.magicthegathering.cards.data.db

import androidx.paging.PagingSource
import androidx.room.*
import com.magicthegathering.cards.domain.entities.MagicCard

@Dao
interface CardsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: MagicCard): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<MagicCard>)

    @Update
    suspend fun updateCard(card: MagicCard)

    @Delete
    suspend fun deleteCard(card: MagicCard)

    @Query("DELETE FROM MagicCard")
    suspend fun clearAllCards()

    @Query("SELECT * FROM MagicCard")
    fun getCards(): PagingSource<Int, MagicCard>

}