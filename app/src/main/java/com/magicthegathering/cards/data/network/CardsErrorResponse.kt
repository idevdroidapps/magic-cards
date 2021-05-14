package com.magicthegathering.cards.data.network

import com.google.gson.annotations.SerializedName

data class CardsErrorResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("error") val error: String
)
