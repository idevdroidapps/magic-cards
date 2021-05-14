package com.magicthegathering.cards.data.network

import com.google.gson.annotations.SerializedName
import com.magicthegathering.cards.domain.entities.MagicCard

data class CardsSuccessResponse(
    @SerializedName("cards") val cards: List<MagicCard>
)
