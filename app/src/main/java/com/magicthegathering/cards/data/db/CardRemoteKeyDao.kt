package com.magicthegathering.cards.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CardRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<CardRemoteKeys>)

    @Query("SELECT * FROM cardremotekeys WHERE id = :id")
    suspend fun remoteKeysCardId(id: String): CardRemoteKeys?

    @Query("DELETE FROM cardremotekeys")
    suspend fun clearRemoteKeys()

}