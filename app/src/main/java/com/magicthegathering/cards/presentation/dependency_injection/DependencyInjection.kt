package com.magicthegathering.cards.presentation.dependency_injection

import androidx.lifecycle.ViewModelProvider
import com.magicthegathering.cards.data.network.MtgService
import com.magicthegathering.cards.data.repositories.CardsRepositoryImpl
import com.magicthegathering.cards.domain.interfaces.CardsRepository
import com.magicthegathering.cards.domain.usecases.CardsUseCases
import com.magicthegathering.cards.presentation.factories.CardsViewModelFactory

object DependencyInjection {

    /**
     * Provides Retrofit Service
     */
    private fun provideMtgService(): MtgService {
        return MtgService.MtgApi.create()
    }

    /**
     * Provides a single source of truth ViewModels
     */
    private fun provideCardsRepository(): CardsRepository {
        return CardsRepositoryImpl.getInstance(provideMtgService())
    }

    /**
     * Provides an instance of [CardsUseCases]
     */
    private fun provideCardsUseCases(): CardsUseCases {
        return CardsUseCases(provideCardsRepository())
    }

    /**
     * Provides the [ViewModelProvider.Factory]
     */
    fun provideCardsViewModelFactory(): ViewModelProvider.Factory {
        return CardsViewModelFactory(provideCardsUseCases())
    }

}