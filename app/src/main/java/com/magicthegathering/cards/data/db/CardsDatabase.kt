package com.magicthegathering.cards.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.magicthegathering.cards.domain.entities.MagicCard

@Database(entities = [MagicCard::class, CardRemoteKeys::class], version = 1, exportSchema = false)
abstract class CardsDatabase : RoomDatabase() {

    abstract val cardsDao: CardsDao
    abstract val remoteKeysDao: CardRemoteKeyDao

    companion object {

        @Volatile
        private var INSTANCE: CardsDatabase? = null

        fun getInstance(context: Context): CardsDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildRoomDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildRoomDatabase(context: Context): CardsDatabase {
            return Room.databaseBuilder(context, CardsDatabase::class.java, "cards_database")
                .build()
        }

    }

}