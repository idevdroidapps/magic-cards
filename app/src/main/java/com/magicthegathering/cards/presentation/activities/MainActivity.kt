package com.magicthegathering.cards.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.magicthegathering.cards.R
import com.magicthegathering.cards.presentation.dependency_injection.DependencyInjection
import com.magicthegathering.cards.presentation.viewmodels.CardsViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewModelProvider(
            this@MainActivity,
            DependencyInjection.provideCardsViewModelFactory(application)
        ).get(CardsViewModel::class.java)

        setContentView(R.layout.activity_main)
    }
}