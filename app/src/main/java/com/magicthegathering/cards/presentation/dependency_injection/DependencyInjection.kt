package com.magicthegathering.cards.presentation.dependency_injection

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.magicthegathering.cards.data.db.CardsDatabase
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
     * Creates an instance of [CardsDatabase]
     */
    private fun provideCardsDatabase(application: Application): CardsDatabase {
        return CardsDatabase.getInstance(application)
    }

    /**
     * Provides a single source of truth ViewModels
     */

    private fun provideCardsRepository(application: Application): CardsRepository {
        return CardsRepositoryImpl.getInstance(
            provideMtgService(),
            provideCardsDatabase(application)
        )
    }

    /**
     * Provides an instance of [CardsUseCases]
     */
    private fun provideCardsUseCases(application: Application): CardsUseCases {
        return CardsUseCases(provideCardsRepository(application))
    }

    /**
     * Provides the [ViewModelProvider.Factory]
     */
    fun provideCardsViewModelFactory(application: Application): ViewModelProvider.Factory {
        return CardsViewModelFactory(provideCardsUseCases(application))
    }

}