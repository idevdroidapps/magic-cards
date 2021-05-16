package com.magicthegathering.cards.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CardRemoteKeys(@PrimaryKey val id: String, val prevKey: Int?, val nextKey: Int?)
