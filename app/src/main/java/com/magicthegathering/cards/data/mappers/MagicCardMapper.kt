package com.magicthegathering.cards.data.mappers

import com.magicthegathering.cards.data.db.CardEntity
import com.magicthegathering.cards.domain.entities.MagicCard

class MagicCardMapper {

    fun toCardEntity(magicCard: MagicCard): CardEntity {
        return CardEntity(
            artist = magicCard.artist,
            cmc = magicCard.cmc,
            colorIdentity = magicCard.colorIdentity,
            colors = magicCard.colors,
            foreignNames = magicCard.foreignNames,
            id = magicCard.id,
            imageUrl = magicCard.imageUrl,
            layout = magicCard.layout,
            manaCost = magicCard.manaCost,
            multiverseid = magicCard.multiverseid,
            name = magicCard.name,
            names = magicCard.names,
            number = magicCard.number,
            originalText = magicCard.originalText,
            originalType = magicCard.originalType,
            power = magicCard.power,
            printings = magicCard.printings,
            rarity = magicCard.rarity,
            rulings = magicCard.rulings,
            set = magicCard.set,
            subtypes = magicCard.subtypes,
            supertypes = magicCard.supertypes,
            text = magicCard.text,
            toughness = magicCard.toughness,
            type = magicCard.type,
            types = magicCard.types
        )
    }

    fun toMagicCard(cardEntity: CardEntity): MagicCard {
        return MagicCard(
            artist = cardEntity.artist,
            cmc = cardEntity.cmc,
            colorIdentity = cardEntity.colorIdentity,
            colors = cardEntity.colors,
            foreignNames = cardEntity.foreignNames,
            id = cardEntity.id,
            imageUrl = cardEntity.imageUrl,
            layout = cardEntity.layout,
            manaCost = cardEntity.manaCost,
            multiverseid = cardEntity.multiverseid,
            name = cardEntity.name,
            names = cardEntity.names,
            number = cardEntity.number,
            originalText = cardEntity.originalText,
            originalType = cardEntity.originalType,
            power = cardEntity.power,
            printings = cardEntity.printings,
            rarity = cardEntity.rarity,
            rulings = cardEntity.rulings,
            set = cardEntity.set,
            subtypes = cardEntity.subtypes,
            supertypes = cardEntity.supertypes,
            text = cardEntity.text,
            toughness = cardEntity.toughness,
            type = cardEntity.type,
            types = cardEntity.types
        )
    }
}