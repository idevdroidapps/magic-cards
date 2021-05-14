package com.magicthegathering.cards.presentation.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.magicthegathering.cards.domain.usecases.CardsUseCases
import com.magicthegathering.cards.presentation.viewmodels.CardsViewModel

class CardsViewModelFactory(private val cardsUseCases: CardsUseCases) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CardsViewModel(cardsUseCases) as T
        }
        throw IllegalArgumentException("Illegal ViewModel Class")
    }
}