package com.magicthegathering.cards.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class MagicCard(
    @field:SerializedName("artist") @ColumnInfo(name = "artist") val artist: String?,
    @field:SerializedName("cmc") @ColumnInfo(name = "cmc") val cmc: Int?,
    @field:SerializedName("id") @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @field:SerializedName("imageUrl") @ColumnInfo(name = "imageUrl") val imageUrl: String?,
    @field:SerializedName("layout") @ColumnInfo(name = "layout") val layout: String?,
    @field:SerializedName("manaCost") @ColumnInfo(name = "manaCost") val manaCost: String?,
    @field:SerializedName("multiverseid") @ColumnInfo(name = "multiverseid") val multiverseid: Int?,
    @field:SerializedName("name") @ColumnInfo(name = "name") val name: String,
    @field:SerializedName("number") @ColumnInfo(name = "number") val number: String?,
    @field:SerializedName("originalText") @ColumnInfo(name = "originalText") val originalText: String?,
    @field:SerializedName("originalType") @ColumnInfo(name = "originalType") val originalType: String?,
    @field:SerializedName("power") @ColumnInfo(name = "power") val power: String?,
    @field:SerializedName("rarity") @ColumnInfo(name = "rarity") val rarity: String?,
    @field:SerializedName("set") @ColumnInfo(name = "set") val `set`: String?,
    @field:SerializedName("text") @ColumnInfo(name = "text") val text: String?,
    @field:SerializedName("toughness") @ColumnInfo(name = "toughness") val toughness: String?,
    @field:SerializedName("type") @ColumnInfo(name = "type") val type: String?,
)