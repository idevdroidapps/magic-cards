package com.magicthegathering.cards.domain.entities

data class ForeignName(
    val language: String,
    val multiverseid: Int,
    val name: String
)